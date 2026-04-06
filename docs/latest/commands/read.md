---
sidebar_position: 3
id: read
title: read
---

# read

Read skill content to stdout. This command is primarily intended for AI agents to consume skill definitions.

## Synopsis

```bash
aiskills read [skill-names] [options]
```

## Interactive Mode (Recommended)

When run without arguments or flags, an interactive prompt guides you through selecting the scope, agent(s), and skill(s) to read. **This is the easiest way to use this command** — no flags to remember:

```bash
aiskills read
```

:::note
Running `aiskills read` with flags but without skill names is an error. Flags are only valid with skill names.
:::

## Non-Interactive Mode

Provide skill name(s) along with `--agent` and `--project`/`--global`. All three are required:

```bash
aiskills read commit --agent claude --project              # Project, Claude
aiskills read commit --agent claude --global               # Global, Claude
aiskills read commit --agent claude --project --global     # Both scopes, Claude
aiskills read commit --agent claude,cursor --project       # Project, Claude + Cursor
aiskills read commit --agent all --project                 # Project, all agents
aiskills read commit --agent all --project --global        # Both scopes, all agents
```

### Reading Multiple Skills

You can read multiple skills in a single command:

```bash
aiskills read commit review-pr --agent claude --project
```

When reading multiple skills, a visual separator is displayed between each skill's output.

## Options

| Flag              | Short | Description                                 |
|-------------------|-------|---------------------------------------------|
| `--agent <names>` | `-a`  | Target agent(s), comma-separated or `all`   |
| `--project`       | `-p`  | Read from project scope (current directory) |
| `--global`        | `-g`  | Read from global scope (home directory)     |

### Valid Agent Names

`universal`, `claude`, `cursor`, `codex`, `gemini`, `windsurf`, `copilot`, or `all`

## Output

The output includes the skill's base directory (for resolving relative references to bundled resources) followed by the full `SKILL.md` content:

```
       Reading: commit
Base directory: /path/to/.claude/skills/commit

---
name: commit
description: Write conventional commit messages
---

(skill content here...)

Skill read: commit
```
