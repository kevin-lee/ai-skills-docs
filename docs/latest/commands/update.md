---
sidebar_position: 4
id: update
title: update
---

# update

Update installed skills by re-fetching them from their original source.

## Synopsis

```bash
aiskills update [skill-names]
```

## Usage

### Update All Skills

```bash
aiskills update
```

Updates every installed skill that has source metadata. Skills from the same Git repository are grouped and cloned only once.

### Update Specific Skills

```bash
aiskills update commit                       # Update a single skill
aiskills update commit review-pr             # Update specific skills
```

## How It Works

When you install a skill, ai-skills records where it came from in a `.aiskills.json` metadata file inside the skill directory. The `update` command uses this metadata to:

1. **Git sources** — clone the original repository, extract the skill, and replace the installed copy.
2. **Local sources** — copy from the original local directory path.

Skills from the same repository are grouped together so the repository is cloned only once, even if multiple skills were installed from it.

## Skipped Skills

The following skills are skipped during update:

| Reason                                         | What to do                                                     |
|------------------------------------------------|----------------------------------------------------------------|
| No source metadata (`.aiskills.json` missing)  | Re-install the skill once to enable updates                    |
| Local source path no longer exists             | Restore the source directory or re-install from a new location |
| `SKILL.md` missing at the recorded source path | Check that the source repository still contains the skill      |
| Missing repo URL in metadata                   | Re-install the skill                                           |
| Git clone failed                               | Check network connectivity and repository access               |

## Output

After updating, a summary is displayed:

```
✅ Updated: commit (project, Claude): .claude/skills
✅ Updated: review-pr (project, Claude): .claude/skills
Summary: 2 updated, 0 skipped (2 total)
```

If any skills were skipped, the reasons are listed:

```
Summary: 1 updated, 1 skipped (2 total)
Missing source metadata (1): old-skill
Re-install these skills once to enable updates (e.g., `aiskills install <source>`).
```
