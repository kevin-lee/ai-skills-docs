---
sidebar_position: 2
id: supported-agents
title: Supported Agents
---

# Supported Agents

`ai-skills` supports the following AI coding agents:

| Agent         | Project Dir         | Global Dir                    |
|---------------|---------------------|-------------------------------|
| **Universal** | `.agents/skills/`   | `~/.agents/skills/`           |
| **Claude**    | `.claude/skills/`   | `~/.claude/skills/`           |
| **Cursor**    | `.cursor/skills/`   | `~/.cursor/skills/`           |
| **Codex**     | `.codex/skills/`    | `~/.codex/skills/`            |
| **Gemini**    | `.gemini/skills/`   | `~/.gemini/skills/`           |
| **Windsurf**  | `.windsurf/skills/` | `~/.codeium/windsurf/skills/` |
| **Copilot**   | `.github/skills/`   | `~/.copilot/skills/`          |

## Project vs Global Scope

Skills can be installed in two scopes:

- **Project** - stored in the current working directory (e.g. `.claude/skills/`). These skills are specific to the project and typically committed to version control so they are shared with collaborators.
- **Global** - stored in your home directory (e.g. `~/.claude/skills/`). These skills are available across all projects on your machine.

When using commands like `install`, `list`, `read`, `sync`, and `remove`, you can specify the scope with the `--project` (`-p`) and `--global` (`-g`) flags. You can pass both flags to target both scopes.

## Custom Global Config Locations

Most supported AI agents can relocate their global config directory with their own environment
variable. `ai-skills` resolves the global skills directory through that variable whenever it is set
and non-blank.

| Agent       | Env Var             | Global Skills Dir when set         |
|-------------|---------------------|------------------------------------|
| **Claude**  | `CLAUDE_CONFIG_DIR` | `$CLAUDE_CONFIG_DIR/skills/`       |
| **Codex**   | `CODEX_HOME`        | `$CODEX_HOME/skills/`              |
| **Gemini**  | `GEMINI_CLI_HOME`   | `$GEMINI_CLI_HOME/.gemini/skills/` |
| **Copilot** | `COPILOT_HOME`      | `$COPILOT_HOME/skills/`            |

`CLAUDE_CONFIG_DIR`, `CODEX_HOME`, and `COPILOT_HOME` point at the config directory itself, while
`GEMINI_CLI_HOME` replaces the home root so `.gemini` is appended to it.

Global `install`, `list`, `read`, `search`, `sync`, `update`, and `remove` all use the relocated
directory.

### How a Relocated Path Is Displayed

- Compact labels use the environment variable form, e.g. `$CLAUDE_CONFIG_DIR/skills`.
- Resolved paths are annotated with where they came from, e.g. `(from $CLAUDE_CONFIG_DIR)`.
- The install location prompt shows the resolved value, e.g. `global  ($CODEX_HOME=~/blah/.codex)`.

### Notes

- **Universal** (`~/.agents`) has no standard environment variable across agents, so it always
  stays under `$HOME`.
- **Cursor**: `CURSOR_CONFIG_DIR` is only documented for the CLI's `cli-config.json`, not for
  skills, so it is not honored.
- **Windsurf** has no documented mechanism to relocate `~/.codeium/windsurf`.
- Project directories are never relocated, since no agent supports a custom project dir location.
- Empty variable values are ignored. A leading `~` expands to your home directory, and relative
  paths resolve against the current directory.
- When an override makes two agents resolve to the same directory, that directory is searched once
  rather than being reported twice.

## Skill Directory Search Priority

When discovering skills, ai-skills searches directories in the following priority order:

| Priority | Path                          | Scope             |
|----------|-------------------------------|-------------------|
| 1        | `./.agents/skills/`           | Project universal |
| 2        | `./.claude/skills/`           | Project Claude    |
| 3        | `./.codex/skills/`            | Project Codex     |
| 4        | `./.github/skills/`           | Project Copilot   |
| 5        | `./.cursor/skills/`           | Project Cursor    |
| 6        | `./.gemini/skills/`           | Project Gemini    |
| 7        | `./.windsurf/skills/`         | Project Windsurf  |
| 8        | `~/.agents/skills/`           | Global universal  |
| 9        | `~/.claude/skills/`           | Global Claude     |
| 10       | `~/.codex/skills/`            | Global Codex      |
| 11       | `~/.copilot/skills/`          | Global Copilot    |
| 12       | `~/.cursor/skills/`           | Global Cursor     |
| 13       | `~/.gemini/skills/`           | Global Gemini     |
| 14       | `~/.codeium/windsurf/skills/` | Global Windsurf   |

Project-scoped skills take priority over global-scoped skills. Within each scope, Universal (`.agents`) is checked first.
After Universal, the remaining agents are ordered by agent name - Claude, Codex, Copilot, Cursor, Gemini, Windsurf - which is why Copilot's `./.github/skills/` comes before Cursor's `./.cursor/skills/`.

When the current directory is your home directory, the project entries are omitted, because they would resolve to the same paths as the global entries.

## AGENTS.md

Some agents - **Universal** and **Codex** - use an `AGENTS.md` file to advertise available skills. ai-skills automatically manages this file:

- When skills are installed or synced to these agents, the `AGENTS.md` file is created or updated with a skills section listing all available skills.
- When skills are removed, the skills section in `AGENTS.md` is updated accordingly.
- For project scope, the file is at `./AGENTS.md`. For global scope, it is at `~/AGENTS.md`.
- The global `AGENTS.md` stays at `~/AGENTS.md` even when `CODEX_HOME` relocates the Codex config directory.

You do not need to edit `AGENTS.md` manually - ai-skills handles it for you.

## Specifying Agents

Most commands accept the `--agent` (`-a`) flag to target specific agent(s):

```bash
# Single agent
aiskills list --agent claude --project

# Multiple agents (comma-separated)
aiskills install owner/repo --agent claude,cursor --project

# All agents
aiskills install owner/repo --agent all --project
```

The valid agent names are: `universal`, `claude`, `cursor`, `codex`, `gemini`, `windsurf`, `copilot`, and the special value `all`.
