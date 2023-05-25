SET REFERENTIAL_INTEGRITY FALSE;
DELETE
FROM ITEM;
DELETE
FROM SHOP;
DELETE
FROM TRACKED_ITEM;
DELETE
FROM ITEM_PRICE;
DELETE
FROM ITEM_TRACKED_ITEMS;
DELETE
FROM TRACKED_ITEM_ITEM_PRICES;
SET REFERENTIAL_INTEGRITY TRUE;

INSERT INTO ITEM(ID, IMAGE, INGREDIENTS, NAME, QUANTITY, AMOUNT, TYPE)
VALUES (1, '', 'zout,peper', 'Pizza hawai', 1, 320, 0);
INSERT INTO ITEM(ID, IMAGE, INGREDIENTS, NAME, QUANTITY, AMOUNT, TYPE)
VALUES (2, '', 'zout,peper,kaas', 'Pizza margherita', 1, 320, 0);

INSERT INTO SHOP(ID, NAME, URL)
VALUES (1, 'Coruyt', '');

INSERT INTO TRACKED_ITEM(URL, ITEM_ID, SHOP_ID)
VALUES ('1_1', 1, 1);
INSERT INTO TRACKED_ITEM(URL, ITEM_ID, SHOP_ID)
VALUES ('1_2', 1, 1);
INSERT INTO TRACKED_ITEM(URL, ITEM_ID, SHOP_ID)
VALUES ('2_1', 2, 1);

INSERT INTO ITEM_PRICE(ID, PRICE, TIMESTAMP, TRACKED_ITEM_URL)
VALUES (1, 3.19, {ts '2023-04-20 12:0:0'}, '1_1');
INSERT INTO ITEM_PRICE(ID, PRICE, TIMESTAMP, TRACKED_ITEM_URL)
VALUES (2, 3.25, {ts '2023-04-22 12:0:0'}, '1_1');
INSERT INTO ITEM_PRICE(ID, PRICE, TIMESTAMP, TRACKED_ITEM_URL)
VALUES (3, 3.22, {ts '2023-04-21 12:0:0'}, '1_2');
INSERT INTO ITEM_PRICE(ID, PRICE, TIMESTAMP, TRACKED_ITEM_URL)
VALUES (4, 3.59, {ts '2023-04-21 12:0:0'}, '2_1');
INSERT INTO ITEM_PRICE(ID, PRICE, TIMESTAMP, TRACKED_ITEM_URL)
VALUES (5, 3.61, {ts '2023-04-21 12:0:1'}, '2_1');

INSERT INTO ITEM_TRACKED_ITEMS(ITEM_ID, TRACKED_ITEMS_URL)
VALUES (1, '1_1');
INSERT INTO ITEM_TRACKED_ITEMS(ITEM_ID, TRACKED_ITEMS_URL)
VALUES (1, '1_2');
INSERT INTO ITEM_TRACKED_ITEMS(ITEM_ID, TRACKED_ITEMS_URL)
VALUES (2, '2_1');

INSERT INTO TRACKED_ITEM_ITEM_PRICES(TRACKED_ITEM_URL, ITEM_PRICES_ID)
VALUES ('1_1', 1);
INSERT INTO TRACKED_ITEM_ITEM_PRICES(TRACKED_ITEM_URL, ITEM_PRICES_ID)
VALUES ('1_1', 2);
INSERT INTO TRACKED_ITEM_ITEM_PRICES(TRACKED_ITEM_URL, ITEM_PRICES_ID)
VALUES ('1_2', 3);
INSERT INTO TRACKED_ITEM_ITEM_PRICES(TRACKED_ITEM_URL, ITEM_PRICES_ID)
VALUES ('2_1', 4);
INSERT INTO TRACKED_ITEM_ITEM_PRICES(TRACKED_ITEM_URL, ITEM_PRICES_ID)
VALUES ('2_1', 5);
