# Business rules

## Category discounts

- ELECTRONICS: 5%
- CLOTHING: 10%
- FOOD: 0%
- BOOKS: 15%

Category discount is calculated per cart line from that line's subtotal.

## Threshold discount

- subtotal < 100: 0%
- subtotal >= 100 and < 500: 3%
- subtotal >= 500: 7%

The threshold is a single tier: at 500 the 7% tier replaces the 3% tier.

## Promo discount

Promo discount is added to category and threshold discounts when the cart subtotal meets the promo minimum. Otherwise `InvalidPromoCodeException` is thrown.

All discount components are added and then capped at the cart subtotal, so the final discount cannot make the payable amount negative.

## Tax

Tax is calculated on the amount after discounts. Because a cart can contain multiple tax categories, the total discount is allocated proportionally across cart lines before applying the category tax rate.

Rates:

- FOOD: 10%
- BOOKS: 0%
- ELECTRONICS: 20%
- CLOTHING: 20%

Money outputs are normalized to two decimal places with `RoundingMode.HALF_UP`.
