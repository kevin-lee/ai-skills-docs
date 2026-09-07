---
sidebar_position: 6
id: sync
title: sync
---

# sync

Copy skills from one agent's directory to another.

## Synopsis

```bash
aiskills sync [skill-names] [options]
```

## Interactive Mode (Recommended)

When run without flags, an interactive wizard guides you through the process. **This is the easiest way to use this command** - no flags to remember:

```bash
aiskills sync
```

The interactive flow is:

1. **Select source location** - `project` or `global`
2. **Select source agent** - filtered to agents that have skills in the selected location
3. **Select skills to sync** - multi-select with all skills pre-selected
4. **Select target location** - `project`, `global`, or `both`
5. **Select target agent(s)** - all agents except the source agent

:::note Tip
In any multi-select prompt, press **Shift+Tab** to toggle select/deselect all. Multi-select prompts require at least one item - pressing Enter with nothing selected shows `Please select at least one, or press Ctrl+C to cancel.` instead of cancelling the operation.
:::

## Non-Interactive Mode

Use `--from`, `--to`, and `--project`/`--global` for scripted usage. All three are required together.

### Sync All Skills

```bash
aiskills sync --from project:claude --to cursor --project                  # Project -> project
aiskills sync --from global:claude --to cursor,windsurf --global           # Global -> global
aiskills sync --from project:claude --to all --project --global            # Project -> both scopes
aiskills sync --from project:universal --to copilot --global -y            # Skip confirmation
```

### Sync Specific Skills

```bash
aiskills sync commit --from global:claude --to cursor --project --global       # One skill
aiskills sync skill1 skill2 skill3 --from global:claude --to codex --project   # Multiple skills
```

## Options

| Flag                        | Short | Description                                                           |
|-----------------------------|-------|-----------------------------------------------------------------------|
| `--from <location>:<agent>` |       | Source location and agent (e.g. `project:claude`, `global:universal`) |
| `--to <agent>`              |       | Target agent(s), comma-separated or `all`                             |
| `--project`                 | `-p`  | Sync to project scope (current directory)                             |
| `--global`                  | `-g`  | Sync to global scope (home directory)                                 |
| `--yes`                     | `-y`  | Skip confirmation prompts                                             |

### `--from` Format

The `--from` flag uses the format `<location>:<agent>` where:
- `<location>` is either `project` or `global`
- `<agent>` is one of: `universal`, `claude`, `cursor`, `codex`, `gemini`, `windsurf`, `copilot`

Examples: `project:claude`, `global:universal`, `global:cursor`

### Valid Agent Names for `--to`

`universal`, `claude`, `cursor`, `codex`, `gemini`, `windsurf`, `copilot`, or `all`

## Overwrite Behavior

If a skill already exists in the target agent's directory, you are prompted with:

- **Yes** - overwrite this skill
- **No** - skip this skill
- **Yes to all** - overwrite this and all remaining conflicts
- **No to all** - skip this and all remaining conflicts

Use `--yes` to automatically overwrite without prompting.

## Duplicate Skill Names - Rename

When a synced skill's name conflicts with an existing one in the target - either with a skill already there, or with another skill being synced in the same run - an interactive prompt offers to rename it. This mirrors the behavior of [`install`](./install.md), letting you, for example, keep both your existing skill and the synced copy under different names.

## Examples

Sync all Claude project skills to Cursor and Windsurf project directories:

```bash
aiskills sync --from project:claude --to cursor,windsurf --project
```

Sync a single skill from Claude global to all agents in both scopes:

```bash
aiskills sync commit --from global:claude --to all --project --global
```
