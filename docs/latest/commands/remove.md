---
sidebar_position: 6
id: remove
title: remove
---

# remove

Remove installed skills.

## Synopsis

```bash
aiskills remove [skill-name] [options]
```

## Interactive Mode

When run without arguments, an interactive prompt guides you through selecting the scope, agent(s), and skill(s) to remove:

```bash
aiskills remove
```

The interactive flow is:

1. **Select scope** — `project`, `global`, or `both`
2. **Select agent(s)** — filtered to agents that have skills in the selected scope
3. **Select skill(s) to remove** — multi-select prompt

## Non-Interactive Mode

Provide a skill name along with `--agent` and `--project`/`--global`. All three are required:

```bash
aiskills remove commit --agent claude --project                  # Project, Claude
aiskills remove commit --agent claude --global                   # Global, Claude
aiskills remove commit --agent claude --project --global         # Both scopes, Claude
aiskills remove commit --agent claude,cursor --project           # Project, Claude + Cursor
aiskills remove commit --agent claude,cursor,windsurf --global   # Global, multiple agents
aiskills remove commit --agent all --project                     # Project, all agents
aiskills remove commit --agent all --project --global            # Everywhere, all agents
```

## Options

| Flag              | Short | Description                               |
|-------------------|-------|-------------------------------------------|
| `--agent <names>` | `-a`  | Target agent(s), comma-separated or `all` |
| `--project`       | `-p`  | Remove from project scope                 |
| `--global`        | `-g`  | Remove from global scope                  |

### Valid Agent Names

`universal`, `claude`, `cursor`, `codex`, `gemini`, `windsurf`, `copilot`, or `all`

## Validation

In non-interactive mode, all three parameters (skill name, `--agent`, and `--project`/`--global`) are required together. Missing any of them results in an error:

```bash
aiskills remove commit                    # ERROR: must specify --project/--global
aiskills remove commit --project          # ERROR: must specify --agent
aiskills remove --agent claude --project  # ERROR: must specify skill name
```

## Output

```
✅ Removed: commit (project, Claude): .claude/skills

✅ Removed 1 skill(s)
```

If a skill is not found at the specified location, a warning is displayed:

```
Warning: Skill 'commit' not found in global/Cursor: ~/.cursor/skills
```
