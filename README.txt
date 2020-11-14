This Checkout application is adapted from a Technical challenge that I did for BJSS.
The ultimate task was the same: to implement a checkout which totals a shopping basket whilst applying discounts,
but the types of discounts they applied and the constraints were different. They too wanted a flexible solution,
so I chose to have the valid PRODUCTS and PROMOTIONS parameterised in flat files and to implement a reasonably generic
algorithm for calculating most common types of promotions. There are several assumptions made about the method of
discount calculation.

Note: the main application tests the cases provided by Not On the High Street, however I have also included the original
tests I wrote for BJSS and supporting data to show how other promotions can be modelled.

ASSUMPTIONS...

1 Discounts can be triggered based on number/types items bought or total spend the number of items of a specific product required to
  trigger a discount is referred to the trigger number likewise the threshold of total spend required to trigger a discount
  is referred to herein as the trigger threshold. (I need to check my terminology).
2 It is assumed that once an item has been involved in triggering one item discount it cannot be involved in another item
  discount. In item which has not been involved in triggering a discount for promotion A can be used to trigger a discount
  for promotion B
3 An item can only have an item specific discount applied to it once
4 The same Promotion can trigger several discounts if for example enough items are bought
5 promotions have priority, in this case promotions defined near the top of the promotion data file have priority over
  those defined below - they are applied in the order in which they occur in the file.
6 A promotion will continue to be applied whilst enough unused triggering items exist.
7 Only one total spend related promotion can be applied a particular basket during the checkout operation.
8 The type of promotion (e.g. total spend or item triggered) are defined by a prefix to the promotion code.
9 Although I've made provision for start and end dates for promotions, these have not been implemented in any.



