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

- **Project** — stored in the current working directory (e.g. `.claude/skills/`). These skills are specific to the project and typically committed to version control so they are shared with collaborators.
- **Global** — stored in your home directory (e.g. `~/.claude/skills/`). These skills are available across all projects on your machine.

When using commands like `install`, `list`, `read`, `sync`, and `remove`, you can specify the scope with the `--project` (`-p`) and `--global` (`-g`) flags. You can pass both flags to target both scopes.

## Skill Directory Search Priority

When discovering skills, ai-skills searches directories in the following priority order:

| Priority | Path                          | Scope             |
|----------|-------------------------------|-------------------|
| 1        | `./.agents/skills/`           | Project universal |
| 2        | `./.claude/skills/`           | Project Claude    |
| 3        | `./.codex/skills/`            | Project Codex     |
| 4        | `./.cursor/skills/`           | Project Cursor    |
| 5        | `./.gemini/skills/`           | Project Gemini    |
| 6        | `./.github/skills/`           | Project Copilot   |
| 7        | `./.windsurf/skills/`         | Project Windsurf  |
| 8        | `~/.agents/skills/`           | Global universal  |
| 9        | `~/.claude/skills/`           | Global Claude     |
| 10       | `~/.codex/skills/`            | Global Codex      |
| 11       | `~/.copilot/skills/`          | Global Copilot    |
| 12       | `~/.cursor/skills/`           | Global Cursor     |
| 13       | `~/.gemini/skills/`           | Global Gemini     |
| 14       | `~/.codeium/windsurf/skills/` | Global Windsurf   |

Project-scoped skills take priority over global-scoped skills. Within each scope, Universal (`.agents`) is checked first.

## AGENTS.md

Some agents — **Universal** and **Codex** — use an `AGENTS.md` file to advertise available skills. ai-skills automatically manages this file:

- When skills are installed or synced to these agents, the `AGENTS.md` file is created or updated with a skills section listing all available skills.
- When skills are removed, the skills section in `AGENTS.md` is updated accordingly.
- For project scope, the file is at `./AGENTS.md`. For global scope, it is at `~/AGENTS.md`.

You do not need to edit `AGENTS.md` manually — ai-skills handles it for you.

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
