import subprocess
import os
import xml.etree.ElementTree as ET
import json
from fastmcp.server.server import FastMCP

print("SERVER.PY IS RUNNING...")

mcp = FastMCP("se333-final-project-server")

@mcp.tool
def add(a: int, b: int) -> int:
    return a + b

@mcp.tool
def run_maven_tests(project_path: str) -> dict:
    MAVEN = r"C:\Users\Joseph Metcalf\Downloads\apache-maven-3.9.11-bin\apache-maven-3.9.11\bin\mvn.cmd"
    if not os.path.isdir(project_path):
        return {"success": False, "error": f"Directory does not exist: {project_path}"}
    try:
        proc = subprocess.run(
            [MAVEN, "test"],
            cwd=project_path,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True,
            shell=False
        )
        output = proc.stdout + "\n" + proc.stderr
        success = (proc.returncode == 0)
        jacoco_report = os.path.join(
            project_path,
            "target",
            "site",
            "jacoco",
            "jacoco.xml"
        )
        return {
            "success": success,
            "return_code": proc.returncode,
            "log": output,
            "jacoco_xml": jacoco_report,
            "jacoco_exists": os.path.exists(jacoco_report)
        }
    except Exception as e:
        return {"success": False, "error": str(e)}

@mcp.tool
def parse_jacoco_xml(xml_path: str) -> dict:
    if not os.path.exists(xml_path):
        return {"success": False, "error": f"XML file not found: {xml_path}"}
    try:
        tree = ET.parse(xml_path)
        root = tree.getroot()
        coverage_result = {"success": True, "packages": []}
        for package in root.findall("package"):
            pkg_data = {"name": package.get("name"), "classes": []}
            for class_elem in package.findall("class"):
                class_data = {
                    "name": class_elem.get("name"),
                    "source": class_elem.get("sourcefilename"),
                    "methods": [],
                    "missed_instructions": 0,
                    "covered_instructions": 0,
                    "missed_branches": 0,
                    "covered_branches": 0
                }
                for counter in class_elem.findall("counter"):
                    ctype = counter.get("type")
                    missed = int(counter.get("missed"))
                    covered = int(counter.get("covered"))
                    if ctype == "INSTRUCTION":
                        class_data["missed_instructions"] += missed
                        class_data["covered_instructions"] += covered
                    elif ctype == "BRANCH":
                        class_data["missed_branches"] += missed
                        class_data["covered_branches"] += covered
                for method in class_elem.findall("method"):
                    method_data = {
                        "name": method.get("name"),
                        "desc": method.get("desc"),
                        "missed_instructions": 0,
                        "covered_instructions": 0,
                        "missed_branches": 0,
                        "covered_branches": 0,
                        "uncovered": False
                    }
                    for mcounter in method.findall("counter"):
                        mtype = mcounter.get("type")
                        mmissed = int(mcounter.get("missed"))
                        mcovered = int(mcounter.get("covered"))
                        if mtype == "INSTRUCTION":
                            method_data["missed_instructions"] += mmissed
                            method_data["covered_instructions"] += mcovered
                        elif mtype == "BRANCH":
                            method_data["missed_branches"] += mmissed
                            method_data["covered_branches"] += mcovered
                    if method_data["missed_instructions"] > 0 or method_data["missed_branches"] > 0:
                        method_data["uncovered"] = True
                    class_data["methods"].append(method_data)
                pkg_data["classes"].append(class_data)
            coverage_result["packages"].append(pkg_data)
        return coverage_result
    except Exception as e:
        return {"success": False, "error": str(e)}

@mcp.tool
def coverage_recommendations(coverage_data: dict) -> dict:
    if not coverage_data.get("success"):
        return {"success": False, "error": "Invalid coverage data"}
    recs = []
    for pkg in coverage_data.get("packages", []):
        for cls in pkg.get("classes", []):
            class_name = cls["name"]
            if cls["missed_branches"] > 0:
                recs.append(
                    f"Class '{class_name}' has {cls['missed_branches']} uncovered branches. "
                    f"Add tests for conditional paths and edge cases."
                )
            for method in cls.get("methods", []):
                if method["uncovered"]:
                    mname = method["name"]
                    recs.append(
                        f"Method '{mname}' in class '{class_name}' has uncovered instructions "
                        f"or branches. Add tests for boundary values, null inputs, and alternate flows."
                    )
    if not recs:
        recs.append("Everything is covered. Great job!")
    return {"success": True, "recommendations": recs}

@mcp.tool
def git_status(repo_path: str) -> dict:
    try:
        result = subprocess.run(
            ["git", "-C", repo_path, "status", "--porcelain=v1"],
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True
        )
        if result.returncode != 0:
            return {"success": False, "error": result.stderr}
        staged = []
        unstaged = []
        untracked = []
        conflicts = []
        for line in result.stdout.splitlines():
            code = line[:2]
            file = line[3:]
            if code == "??":
                untracked.append(file)
            elif "U" in code:
                conflicts.append(file)
            elif code[0] != " ":
                staged.append(file)
            elif code[1] != " ":
                unstaged.append(file)
        return {
            "success": True,
            "staged": staged,
            "unstaged": unstaged,
            "untracked": untracked,
            "conflicts": conflicts
        }
    except Exception as e:
        return {"success": False, "error": str(e)}

@mcp.tool
def git_add_all(repo_path: str) -> dict:
    excludes = [
        ":!target/",
        ":!*.class",
        ":!.DS_Store",
        ":!node_modules/",
        ":!.pytest_cache/",
        ":!*.log"
    ]
    try:
        cmd = ["git", "-C", repo_path, "add", "."]
        for ex in excludes:
            cmd.append(ex)
        result = subprocess.run(
            cmd,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True
        )
        if result.returncode != 0:
            return {"success": False, "error": result.stderr}
        return {"success": True, "message": "Files staged successfully."}
    except Exception as e:
        return {"success": False, "error": str(e)}

@mcp.tool
def git_commit(repo_path: str, message: str) -> dict:
    try:
        result = subprocess.run(
            ["git", "-C", repo_path, "commit", "-m", message],
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True
        )
        if "nothing to commit" in result.stdout.lower():
            return {"success": False, "error": "Nothing to commit."}
        if result.returncode != 0:
            return {"success": False, "error": result.stderr}
        return {"success": True, "output": result.stdout}
    except Exception as e:
        return {"success": False, "error": str(e)}

@mcp.tool
def git_push(repo_path: str, remote: str = "origin") -> dict:
    try:
        result = subprocess.run(
            ["git", "-C", repo_path, "push", remote, "HEAD"],
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True
        )
        if result.returncode != 0:
            return {"success": False, "error": result.stderr}
        return {"success": True, "output": result.stdout}
    except Exception as e:
        return {"success": False, "error": str(e)}

@mcp.tool
def git_pull_request(repo_path: str, base: str = "main", title: str = "Automated PR", body: str = "") -> dict:
    try:
        result = subprocess.run(
            ["gh", "pr", "create", "--base", base, "--title", title, "--body", body],
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            text=True,
            cwd=repo_path
        )
        if result.returncode != 0:
            return {"success": False, "error": result.stderr}
        return {"success": True, "pr_url": result.stdout.strip()}
    except Exception as e:
        return {"success": False, "error": str(e)}

@mcp.tool
def generate_spec_tests(method_signature: str, specification: str) -> dict:
    try:
        header = method_signature.strip()
        name_start = header.find("(")
        name_end = header.find(")")
        method_name = header.split("(")[0].split()[-1]
        params_raw = header[name_start+1:name_end].strip()

        params = []
        if params_raw:
            for p in params_raw.split(","):
                p = p.strip()
                ptype, pname = p.split()
                params.append((ptype, pname))

        test_cases = []
        for ptype, pname in params:
            if ptype in ["int", "Integer"]:
                test_cases.append({"input": f"{pname} = 0", "reason": "boundary"})
                test_cases.append({"input": f"{pname} = 1", "reason": "boundary"})
                test_cases.append({"input": f"{pname} = -1", "reason": "boundary"})
                test_cases.append({"input": f"{pname} = large positive", "reason": "equivalence"})
                test_cases.append({"input": f"{pname} = large negative", "reason": "equivalence"})

            elif ptype in ["double", "float"]:
                test_cases.append({"input": f"{pname} = 0.0", "reason": "boundary"})
                test_cases.append({"input": f"{pname} = negative", "reason": "equivalence"})
                test_cases.append({"input": f"{pname} = positive", "reason": "equivalence"})

            elif ptype == "String":
                test_cases.append({"input": f'{pname} = ""', "reason": "boundary"})
                test_cases.append({"input": f"{pname} = null", "reason": "equivalence"})
                test_cases.append({"input": f'{pname} = "validValue"', "reason": "equivalence"})

        junit_code = f"""
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class {method_name}SpecTest {{

    {specification}

"""

        for idx, case in enumerate(test_cases):
            junit_code += f"""
    @Test
    void test_case_{idx+1}() {{
        // Reason: {case["reason"]}
        // Input suggestion: {case["input"]}
    }}
"""

        junit_code += "\n}\n"

        return {
            "success": True,
            "method": method_signature,
            "generated_tests": junit_code
        }

    except Exception as e:
        return {"success": False, "error": str(e)}

if __name__ == "__main__":
    mcp.run(transport="sse", host="127.0.0.1", port=8000)