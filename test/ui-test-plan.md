# UI test plan

The `test-ui` skill runs each case in a fresh Bkxss session. Inputs are sent in
order, followed by `bye`. Expected output includes only the responses to the
listed inputs, not the greeting or farewell.

## Test case: Create todo task

Aim: Verify that a todo command creates and displays a Todo task.

### Inputs

```text
todo borrow book
```

### Expected output

```text
Got it. I've added this task:
[T][ ] borrow book
Now you have 1 tasks in the list.
```

## Test case: Create deadline task

Aim: Verify that a deadline command preserves its description and due date.

### Inputs

```text
deadline return book /by Sunday
```

### Expected output

```text
Got it. I've added this task:
[D][ ] return book (by: Sunday)
Now you have 1 tasks in the list.
```

## Test case: Create event task

Aim: Verify that an event command preserves its description, start, and end times.

### Inputs

```text
event project meeting /from Mon 2pm /to 4pm
```

### Expected output

```text
Got it. I've added this task:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 1 tasks in the list.
```

## Test case: List polymorphic tasks

Aim: Verify that the Task array lists Todo, Deadline, and Event objects using their own output formats.

### Inputs

```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
```

### Expected output

```text
Got it. I've added this task:
[T][ ] borrow book
Now you have 1 tasks in the list.
Got it. I've added this task:
[D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
Got it. I've added this task:
[E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
Here are the tasks in your list:
1.[T][ ] borrow book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
```
