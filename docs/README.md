# Bkxss User Guide

Bkxss is a personal task assistant that stores todos, deadlines, and events.

## Finding free times

Use `findfree` to find the earliest continuous free slot inside a search range:

`findfree HOURS /from START /to END`

`HOURS` must be a positive whole number. `START` and `END` use the
`yyyy-MM-dd HHmm` format. For example:

```text
findfree 4 /from 2026-09-12 0900 /to 2026-09-13 1800
```

Bkxss compares the range with all saved events and reports the earliest slot
that is at least four hours long. Todos and deadlines do not occupy time, and
an event still occupies time when it is marked as done.

Events need complete dates to participate in schedule calculations:

```text
event project meeting /from 2026-09-12 1000 /to 2026-09-12 1200
```

Older events that use free-text times, such as `Mon 2pm`, can still be loaded
and displayed. Bkxss asks you to replace such an event with complete dates
before calculating free time, because its position on the calendar is unknown.

The search range is continuous and may cross midnight. Bkxss does not assume
working or sleeping hours, so include only the hours you want searched.

## Adding deadlines

Use `deadline DESCRIPTION /by DATE` to add a task with a due date. Dates use
the `yyyy-MM-dd HHmm` format.

```text
deadline return book /by 2026-09-15 1800
```

## Other commands

- `todo DESCRIPTION`: add a todo
- `event DESCRIPTION /from START /to END`: add an event
- `list`: show all tasks
- `find KEYWORD`: find tasks by description
- `mark NUMBER` and `unmark NUMBER`: change a task's completion status
- `delete NUMBER`: remove a task
- `bye`: exit Bkxss
