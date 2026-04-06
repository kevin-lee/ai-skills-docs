---
sidebar_position: 1
id: getting-started
title: Getting Started
slug: /
---

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

![](/img/ai-skills-all.svg)

[![[Scala Native] Build All](https://github.com/kevin-lee/ai-skills/actions/workflows/build-native.yml/badge.svg)](https://github.com/kevin-lee/ai-skills/actions/workflows/build-native.yml)
[![Release](https://github.com/kevin-lee/ai-skills/actions/workflows/release.yml/badge.svg)](https://github.com/kevin-lee/ai-skills/actions/workflows/release.yml)
![GitHub Release](https://img.shields.io/github/v/release/kevin-lee/ai-skills)


[![Hits](https://hits.sh/github.com/kevin-lee/ai-skills.svg)](https://ai-skills.kevinly.dev)

## What is ai-skills?

**ai-skills** is a native CLI tool for managing reusable prompt skills for AI coding agents. It can install and manage AI agent skills (`SKILL.md`) and provides commands to install, list, read, update, sync, and remove skills across project-local and global directories for multiple AI agents.

Built with Scala 3 and Scala Native, it compiles to a standalone binary with no JVM or Node.js runtime required.

### Why ai-skills?

AI coding agents like Claude, Cursor, Codex, Gemini, Windsurf, and Copilot each support skills (reusable prompt instructions), but they store them in different directories. **ai-skills** gives you a single tool to:

- **Install** skills from GitHub repos, Git URLs, or local directories
- **Sync** skills across multiple agents with one command
- **Update** all your skills from their original sources
- **Manage** skills consistently across project and global scopes

## Installation
<Tabs>
<TabItem value="homebrew" label="Homebrew (macOS / Linux)" default>

### Install `ai-skills`

Install directly (taps automatically) [**Recommended**]:

```bash
brew install kevin-lee/tap/ai-skills
```

***

Or tap first, then install:

```bash
brew tap kevin-lee/tap
brew install ai-skills
```

### Update `ai-skills`
If it's already installed and you want to update to the latest version,
```bash
brew update && brew upgrade ai-skills
```


</TabItem>
<TabItem value="github-releases" label="GitHub Releases">

Pre-built binaries are available on the [Releases](https://github.com/kevin-lee/ai-skills/releases) page.

| Platform        | Binary                    |
|-----------------|---------------------------|
| macOS 26+ ARM64 | `aiskills-macos-26-arm64` |
| macOS 15 ARM64  | `aiskills-macos-15-arm64` |
| Linux ARM64     | `aiskills-linux-arm64`    |
| Linux x86_64    | `aiskills-linux-x86_64`   |

```bash
# Example: download and install on macOS ARM64
curl -L -o aiskills https://github.com/kevin-lee/ai-skills/releases/latest/download/aiskills-macos-26-arm64
chmod +x aiskills
sudo mv aiskills /usr/local/bin/
```

</TabItem>
</Tabs>

Verify the installation:

```bash
aiskills --version
```
```
@VERSION@
```

## Interactive Mode — The Recommended Way

Most ai-skills commands support **interactive mode** — just run the command without any flags and you'll be guided through each step with prompts. This is the recommended way to use ai-skills, as you don't need to memorise any flags or parameters.

```bash
aiskills install owner/repo   # Prompts for agent(s) and location
aiskills list                 # Prompts for scope and agent(s)
aiskills read                 # Prompts for scope, agent(s), and skill(s)
aiskills sync                 # Guided wizard for source/target selection
aiskills remove               # Prompts for scope, agent(s), and skill(s)
```

For scripting and CI/CD, non-interactive mode is also available — see each command's documentation for the full set of flags.

## Quick Start

### 1. Install skills from a GitHub repo

```bash
aiskills install owner/repo
```

An interactive prompt lets you choose which agent(s) and location (project or global) to install to.

e.g.) Install skills from Anthropic's Claude repo:

```bash
aiskills install anthropic/claude-skills
```

### 2. List installed skills

```bash
aiskills list
```

### 3. Read a skill

Interactive Mode:
```bash
aiskills read
```

Non-Interactive Mode:
```bash
aiskills read skill-name --agent claude --project
```

This outputs the skill content to stdout, intended for AI agents to consume.

### 4. Update all skills

```bash
aiskills update
```

Re-fetches skills from their original source.

### 5. Sync skills between agents

Interactive Mode:
```bash
aiskills sync
```

Non-Interactive Mode:
```bash
aiskills sync --from project:claude --to cursor --project
```

Copies skills from one agent's directory to another.

### 6. Remove a skill

```bash
aiskills remove
```

Opens an interactive prompt to select skills for removal.
