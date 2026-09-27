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

## Interactive Mode (Recommended)

When run without any flags, an interactive prompt lets you choose the scope (project, global, or both) and the agent(s) to display. **This is the easiest way to use this command** - no flags to remember:

```bash
aiskills list
```

If skills exist in only one scope or for only one agent, that selection is made automatically.

:::note Tip
In any multi-select prompt, press **Shift+Tab** to toggle select/deselect all.
:::

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

The output displays each skill with its scope, agent, directory path, and source metadata:

```
Available Skills:

  commit                    (project, Claude): .claude/skills
  Base directory: ~/git/username/path/to/project/.claude/skills/commit
      sourceType: git
          source: owner/commit-skills
         subpath: skills/commit
          commit: 3f9c2a1d8e7b6c5a4f3e2d1c0b9a8f7e6d5c4b3a
      sourceHash: 5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3d4e
            name: commit
    Write conventional commit messages

  pdf                       (project, Gemini): .gemini/skills
  Base directory: ~/git/username/path/to/project/.gemini/skills/pdf
      sourceType: git
          source: anthropics/skills
         subpath: skills/pdf
          commit: 8c1e4b7a2d5f9e3c6b0a4d8f2e7c1b5a9d3f6e0c
      sourceHash: b2d4f6a8c0e2a4c6e8b0d2f4a6c8e0b2d4f6a8c0
            name: pdf
    Use this skill whenever the user wants to do anything with PDF files.

Summary: 2 project, 0 global (2 total)
```

See [`read`](./read.md) for the meaning of each metadata field. `commit`, `sourceHash` and `checkedAt` are shown only when they are recorded.

When an agent's global directory is relocated by its environment variable, the `Base directory` line is annotated with `(from $ENV_VAR)`. See [Custom Global Config Locations](../supported-agents.md#custom-global-config-locations).
