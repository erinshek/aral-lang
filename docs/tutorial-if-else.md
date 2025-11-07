---
layout: default
title: If-else
parent: Tutorial
nav_order: 3
---

# If-else

Conditional statements in the Aral programming language

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