---
layout: default
title: If...else
parent: Tutorial
nav_order: 5
---

# If Statement

Aral supports the following logical conditions:

 - Equals: `a == b`
 - Not equals: `a != b`
 - Less than: `a < b` 
 - Less than or equal to: `a <= b` 
 - Greater than: `a > b` 
 - Greater than or equal to: `a >= b`

## Example

```aral
ózgeriwshi san a = 5;
ózgeriwshi san b = 3;

eger (a > b) bolsa {
    shıǵar("a is greater than b");
} ol bolmasa (b > a) {
    shıǵar("b is greater than a");
} dım bolmasa {
    shıǵar("a and b are equal");
}
```