SE333 Final Project – Autonomous MCP Testing Agent

Author: Joseph Metcalf
Course: SE333

Overview

This project implements an autonomous Multi-Agent Control Protocol (MCP) testing agent that analyzes a Java
codebase, runs tests, evaluates coverage, generates improvements, and automates git operations. The work includes
the MCP server, testing tools, the iterative improvement agent, and a creative MCP extension.

The system is composed of:

- An MCP server (server.py)

- A suite of MCP tools for testing, coverage, and git automation (Phase Three)

- An autonomous iterative test-improvement agent (Phase Four)

- A creative extension tool for specification-based test generation (Phase Five)

- A Java codebase used as the main analysis target

The agent is capable of:

- Running Maven tests

- Analyzing failures

- Parsing coverage

- Generating new tests

- Fixing bugs when needed

- Committing improvements

- Repeating until no further improvements are possible

Project Structure:

- final_project_se333_JosephMetcalf/

  |.github/prompts/tester.prompt.md

  |.vscode/mcp.json

  |codebase/

  |server.py

  |README.md

MCP Tools Implemented

1. run_maven_tests

- Runs the Maven test suite for any Java project

- Returns logs, return code, and JaCoCo paths

2. parse_jacoco_xml

- Parses JaCoCo coverage XML files

- Extracts class, method, instruction, and branch coverage

3. coverage_recommendations

- Generates human-readable recommendations based on uncovered classes and methods

4. Git Automation Tools

- git_status, git_add_all, git_commit, git_push, git_pull_request, generate_spec_tests (Phase Five extension)

These tools enable test automation, coverage analysis, and repository manipulation.

Phase Two - MCP Server

Activating virtual environment

1. cd final_project_se333_JosephMetcalf (Sometimes I had to ovrride my permissions)

2. ..venv\Scripts\activate

3. python server.py

To connect to VS Code:

1. Open VS Code

2. Go to MCP settings

3. Add MCP server in VS Code via HTTP (SSE) at: http://127.0.0.1:8000/sse

Phase Four – Iterative Testing Agent

Defined in .github/prompts/tester.prompt.md.

The agent performs a continuous improvement loop:

- Run tests

- Analyze failures

- Parse coverage

- Apply recommendations

- Generate or fix tests

- Commit changes

- Repeat

- Notes on Behavior

The agent successfully performed:

- Test execution

- Coverage recommendation parsing

- Automated creation of new test files

- Git commits of improved test suites

I faced challenges when trying to use codebase however, due to the AI not being able to sustain
how much information it was processing

Phase Five – Creative Extension
Specification-Based Test Generator

A new MCP tool, generate_spec_tests, was implemented to generate JUnit test skeletons based on:

- Method signatures

- Natural-language specifications

- Boundary value analysis

- Equivalence class partitioning

This extension worked very well when used with the Assignment Five project.

Success on Assignment 5 Codebase

To validate the system:

- Tools executed correctly

- Coverage parsed cleanly

- Generated tests were correct

- No unexpected failures occurred

Because it behaved reliably, this codebase will be showcased in the final demo.

Conclusion

This project delivers:

- A working MCP server

- Fully functional testing and git tools

- A specification-based test generator

- An iterative test-improvement agent

While the large Apache Commons Lang codebase proved challenging for full automation, the MCP agent functioned
successfully on smaller projects, confirming the design and implementation.
