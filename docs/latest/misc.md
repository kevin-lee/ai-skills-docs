---
sidebar_position: 4
id: misc
title: Miscellaneous
---

# Miscellaneous

## Source Metadata

When you install a skill using ai-skills, a `.aiskills.json` file is automatically created inside the skill directory. This file records where the skill was installed from:

```json
{
  "source": "owner/repo",
  "sourceType": "git",
  "repoUrl": "https://github.com/owner/repo.git",
  "subpath": "skills/my-skill",
  "localPath": null,
  "installedAt": "2026-04-01T12:00:00Z"
}
```

This metadata enables the [`update`](commands/update.md) command to re-fetch skills from their original source. You do not need to create or edit this file manually — it is managed by ai-skills.

:::info
Skills installed before source metadata tracking was added will not have a `.aiskills.json` file. Re-install them once to enable updates.
:::
