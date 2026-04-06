---
sidebar_position: 1
id: list
title: list
---

# list

List installed skills with optional filtering by scope and agent.

## Synopsis

```bash
aiskills list [options]
```

## Interactive Mode

When run without any flags, an interactive prompt lets you choose the scope (project, global, or both) and the agent(s) to display:

```bash
aiskills list
```

If skills exist in only one scope or for only one agent, that selection is made automatically.

## Non-Interactive Mode

Use `--project` and/or `--global` to specify the scope. When `--agent` is provided, `--project` and/or `--global` must also be specified.

```bash
aiskills list --project                          # Project skills, all agents
aiskills list --global                           # Global skills, all agents
aiskills list --project --global                 # Both scopes, all agents
aiskills list --agent claude --project           # Project skills, Claude only
aiskills list --agent all --project              # Project skills, all agents (no prompt)
aiskills list --agent all --global               # Global skills, all agents
aiskills list --agent all --project --global     # Both scopes, all agents
```

## Options

| Flag              | Short | Description                                  |
|-------------------|-------|----------------------------------------------|
| `--project`       | `-p`  | Show project skills only                     |
| `--global`        | `-g`  | Show global skills only                      |
| `--agent <names>` | `-a`  | Filter by agent(s), comma-separated or `all` |

### Valid Agent Names

`universal`, `claude`, `cursor`, `codex`, `gemini`, `windsurf`, `copilot`, or `all`

## Output

The output displays each skill's name, scope, agent, and directory path:

```
Available Skills:

  commit                    (project, Claude): .claude/skills
    Write conventional commit messages

  review-pr                 (global, Claude): ~/.claude/skills
    Review pull requests for best practices

Summary: 1 project, 1 global (2 total)
```
