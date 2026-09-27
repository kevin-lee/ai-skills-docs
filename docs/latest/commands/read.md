---
sidebar_position: 4
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

When run without arguments or flags, an interactive prompt guides you through selecting the scope, agent(s), and skill(s) to read. **This is the easiest way to use this command** - no flags to remember:

```bash
aiskills read
```

:::note
Running `aiskills read` with flags but without skill names is an error. Flags are only valid with skill names.
:::

:::note Tip
In any multi-select prompt, press **Shift+Tab** to toggle select/deselect all.
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

The output starts with metadata about the skill, followed by the full `SKILL.md` content. Paths under your home directory are abbreviated with `~/`.

```
         Reading: commit
  Base directory: ~/git/username/path/to/project/.claude/skills/commit
      sourceType: git
          source: owner/commit-skills
         subpath: skills/commit
          commit: 3f9c2a1d8e7b6c5a4f3e2d1c0b9a8f7e6d5c4b3a
      sourceHash: 5d6e7f8a9b0c1d2e3f4a5b6c7d8e9f0a1b2c3d4e
            name: commit

---
name: commit
description: Write conventional commit messages
---

(skill content here...)

Skill read: commit
```

### Output Field Reference

| Field            | Meaning                                                            |
|------------------|--------------------------------------------------------------------|
| `Reading`        | The skill name as installed (its directory name)                   |
| `Base directory` | Path to the skill on disk (uses `~/` for paths under your home, and is annotated with `(from $ENV_VAR)` when the agent's [global directory is relocated](../supported-agents.md#custom-global-config-locations)) |
| `sourceType`     | Origin type: `git` or `local`                                      |
| `source`         | Origin reference (e.g. `owner/repo` for git, a path for local)     |
| `subpath`        | Path within the source where `SKILL.md` lives. A skill at the repository root shows `<root>` |
| `commit`         | The Git commit the installed content came from (Git sources only)  |
| `sourceHash`     | The recorded version of the skill: the Git tree hash of its source folder. See [Version Check](./update.md#version-check) |
| `checkedAt`      | When `update` last checked the skill (global skills only)          |
| `name`           | The `name` field from `SKILL.md` (may differ from the folder name) |

`commit`, `sourceHash` and `checkedAt` are shown only when they are recorded. Skills installed by 2.10.0 or earlier show them after their next update.
