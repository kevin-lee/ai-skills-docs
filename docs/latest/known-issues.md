---
sidebar_position: 5
id: known-issues
title: Known Issues
---

# Known Issues

This page lists known issues that may affect your experience with ai-skills. Most are caused by upstream dependencies and are tracked accordingly.

## Multi-Choice List Rendering

When ai-skills displays an interactive multi-choice list — used in commands like `install`, `list`, `read`, `remove`, `sync`, and `search` — the list may render incorrectly if there is not enough vertical space below the cursor in your terminal.

:::info NOTE
This is a **display-only** issue. ai-skills itself continues to work correctly — your selections, installs, and other operations are unaffected. Only the on-screen rendering looks wrong.
:::

**When it happens:** The bad rendering is triggered when an **error message** is displayed below the multi-choice list — for example, when you press Enter without selecting anything and the prompt shows `Please select at least one, or press Ctrl+C to cancel.` If no error message ever appears, the list renders correctly.

<video controls width="100%" muted playsinline>
  <source src="https://github.com/user-attachments/assets/add8f341-9043-4c24-8d10-0ed6f22315b5" type="video/mp4" />
  Your browser does not support the video tag.
</video>

**Cause:** This is an upstream issue in the [cue4s](https://github.com/neandertech/cue4s) library, tracked at [neandertech/cue4s#58](https://github.com/neandertech/cue4s/issues/58).

**Workaround:** Clear the terminal first and start using `ai-skills` from a freshly cleared screen. Common shortcuts to clear:

- **macOS (iTerm2 / Terminal.app):** `Cmd+K`
- **Linux / Windows terminals:** `Ctrl+L` (or run `clear`)

If you still see the issue, also increase your terminal window height so there is enough empty space below the prompt.
