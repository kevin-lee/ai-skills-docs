---
sidebar_position: 2
id: install
title: install
---

# install

Install skills from a GitHub repo, Git URL, or local directory.

## Synopsis

```bash
aiskills install <source> [options]
```

## Source Types

### GitHub Shorthand

```bash
aiskills install owner/repo                  # All skills in the repo (for both public and private)
aiskills install owner/repo/skill-name       # A specific skill by path
```

### Git URL

```bash
aiskills install https://github.com/owner/repo.git    # HTTPS
aiskills install git@github.com:owner/repo.git        # SSH
```

### Local Path

```bash
aiskills install ./path/to/skill-directory
aiskills install ~/my-skills/my-skill
```

## Interactive Mode

When `--agent` is not provided, an interactive prompt lets you choose the target agent(s) and location before cloning/installing:

```bash
aiskills install owner/repo
```

The prompts appear **before** the repository is cloned, so you don't need to wait for the clone to complete before making your selections.

## Non-Interactive Mode

Specify `--agent` along with `--project` and/or `--global`:

```bash
aiskills install owner/repo --agent claude --project           # Project, Claude
aiskills install owner/repo --agent claude --global            # Global, Claude
aiskills install owner/repo --agent claude --project --global  # Both scopes, Claude
aiskills install owner/repo --agent claude,cursor --project    # Project, Claude + Cursor
aiskills install owner/repo --agent all --project              # Project, all agents
aiskills install owner/repo --agent all --global               # Global, all agents
aiskills install owner/repo --agent all --project --global     # Both scopes, all agents
```

## Options

| Flag              | Short | Description                                          |
|-------------------|-------|------------------------------------------------------|
| `--agent <names>` | `-a`  | Target agent(s), comma-separated or `all`            |
| `--project`       | `-p`  | Install to project scope (current directory)         |
| `--global`        | `-g`  | Install to global scope (home directory)             |
| `--yes`           | `-y`  | Skip interactive selection, install all skills found |

### Valid Agent Names

`universal`, `claude`, `cursor`, `codex`, `gemini`, `windsurf`, `copilot`, or `all`

## Skill Selection

When a repository contains multiple skills, an interactive multi-select prompt lets you choose which skills to install (unless `--yes` is passed):

```bash
aiskills install owner/repo                    # Prompted to select skills
aiskills install owner/repo -y                 # Install all skills without prompting
```

## Overwrite Behavior

If a skill with the same name already exists at the target location, you are prompted with:

- **Yes** — overwrite this skill
- **No** — skip this skill
- **Yes to all** — overwrite this and all remaining conflicts
- **No to all** — skip this and all remaining conflicts

Use `--yes` to automatically overwrite all existing skills without prompting.

## Private Repositories

When installing from a GitHub HTTPS URL, if the clone fails (e.g. the repo is private), ai-skills automatically falls back to the equivalent SSH URL (`git@github.com:owner/repo.git`). Ensure your SSH keys are configured for this to work.

```bash
# These all work with private repos if SSH is configured:
aiskills install owner/private-repo
aiskills install git@github.com:owner/private-repo.git
```

:::caution Marketplace Skill Conflicts
When installing a skill globally whose name matches an [Anthropic marketplace skill](https://docs.anthropic.com/en/docs/claude-code/skills) (e.g. `pdf`, `xlsx`), a warning is displayed. If you later re-enable Claude plugins, the marketplace version may overwrite your global skill. Use `--project` for conflict-free installation.
:::
