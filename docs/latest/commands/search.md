---
sidebar_position: 3
id: search
title: search
---

# search

Search for skills in your local installations or in the marketplace.

## Synopsis

```bash
aiskills search [search-query] [options]
```

## Interactive Mode (Recommended)

When run without flags (or with only a search query), an interactive prompt guides you through choosing where to search and entering a query. **This is the easiest way to use this command** — no flags to remember:

```bash
aiskills search                   # Pick where to search, then enter query
aiskills search pdf               # Pick where to search, query is pre-filled
```

The interactive flow is:

1. **Search from** — `marketplace` or `local`
2. **Enter search query** — free-text query (skipped if already provided as an argument)
3. **Select skill(s)** — multi-select prompt of matching skills
4. **Choose an action** — `read`, `install` (marketplace only), `list`, or `finish`

Example:

```
$ aiskills search
✔ Search from  … marketplace
✔ Enter search query … pdf
Searching marketplaces for: pdf

   ✔ Found 39 skill(s)

? Select skill(s) ›
Tab to toggle, Shift+Tab to toggle all, Enter to submit.
 ◯ pdf                       anthropics/skills              51.2K installs [skills.sh]
 ◯ pdftk-server              github/awesome-copilot         6.5K installs [skills.sh]
 ◯ nano-pdf                  steipete/clawdis               1.6K installs [skills.sh]
 ◯ view-pdf                  anthropics/knowledge-work-plugins 354 installs [skills.sh]
```

:::note Tip
In any multi-select prompt, press **Shift+Tab** to toggle select/deselect all.
:::

## Non-Interactive Mode

Provide the search query and exactly one of `--marketplace` or `--local`:

```bash
aiskills search <search-query> --marketplace   # Search the marketplace
aiskills search <search-query> --local         # Search local skills (all agents, both scopes)
```

### Invalid Combinations

```bash
aiskills search <search-query> --local --marketplace   # ERROR: choose one
aiskills search --local                                # ERROR: <search-query> is required
aiskills search --marketplace                          # ERROR: <search-query> is required
```

## Options

| Flag            | Description                                                  |
|-----------------|--------------------------------------------------------------|
| `--marketplace` | Search marketplace skills                                    |
| `--local`       | Search local skills (all agents, both global and project)    |

`--marketplace` and `--local` are mutually exclusive.

## Search Results — Marketplace

Marketplace results include each skill's source metadata, install count, and the marketplace it came from:

```
Search Results:

  pdf               anthropics/skills
      sourceType: git
          source: anthropics/skills
         subpath: skills/pdf
            name: pdf
    Use this skill whenever the user wants to do anything with PDF files.
    Installs: 51.2K installs  Marketplace: skills.sh

1 skill(s) shown
```

## Search Results — Local

Local results include each skill's scope, agent, base directory, and source metadata:

```
Found 1 matching skill(s) for 'pdf':

  pdf-converter             (project, Cursor): .cursor/skills
  Base directory: ~/git/username/path/to/project/.cursor/skills/pdf-converter
      sourceType: git
          source: claude-office-skills/skills
         subpath: pdf-converter
            name: PDF Converter
    Convert PDFs to other formats and back.
```

## Post-Search Actions

After you select skills from the results, ai-skills asks what you want to do with them:

- **`read`** — Print the selected skills' content to stdout, then return to the action menu.
- **`list`** — List the selected skills with their metadata, then return to the action menu.
- **`install`** — (Marketplace only) Install the selected skills, then finish.
- **`finish`** — End the search session.

`read` and `list` loop back to the action menu so you can perform multiple actions on the same selection. `install` and `finish` end the flow. For local results, `install` is not offered because the skills are already installed.
