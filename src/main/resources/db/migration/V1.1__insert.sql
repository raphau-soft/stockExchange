INSERT INTO user VALUES(1, 'Piotr', 'Nowak', 'x1', 'mypass', 'ROLE_USER', 2000, 'xyz1@gmail.com');
INSERT INTO user VALUES(2, 'Jan', 'Nowak', 'x2', 'mypass', 'ROLE_USER', 2000, 'xyz2@gmail.com');
INSERT INTO user VALUES(3, 'Karolina', 'Janusz', 'x3', 'mypass', 'ROLE_USER', 2000, 'xyz3@gmail.com');
INSERT INTO user VALUES(4, 'Karol', 'Król', 'x4', 'mypass', 'ROLE_USER', 2000, 'xyz4@gmail.com');
INSERT INTO user VALUES(5, 'Kamil', 'Daniel', 'x5', 'mypass', 'ROLE_USER', 2000, 'xyz5@gmail.com');
INSERT INTO user VALUES(6, 'Patryk', 'Man', 'x6', 'mypass', 'ROLE_USER', 2000, 'xyz6@gmail.com');
INSERT INTO user VALUES(7, 'Rafał', 'Kos', 'x7', 'mypass', 'ROLE_USER', 2000, 'xyz7@gmail.com');
INSERT INTO user VALUES(8, 'Robert', 'Biały', 'x8', 'mypass', 'ROLE_USER', 2000, 'xyz8@gmail.com');
INSERT INTO user VALUES(9, 'Paulina', 'Smolak', 'x9', 'mypass', 'ROLE_USER', 2000, 'xyz9@gmail.com');
INSERT INTO user VALUES(10, 'Paweł', 'Kasztan', 'x10', 'mypass', 'ROLE_USER', 2000, 'xyz10@gmail.com');

INSERT INTO company VALUES(1, 'Amper');
INSERT INTO company VALUES(2, 'Volt');
INSERT INTO company VALUES(3, 'Antras');
INSERT INTO company VALUES(4, 'Fach-Bud');
INSERT INTO company VALUES(5, 'Geitz');
INSERT INTO company VALUES(6, 'Lindrus');

INSERT INTO stock VALUES(1, 1, null, 100);
INSERT INTO stock VALUES(2, 2, null, 200);
INSERT INTO stock VALUES(3, 3, null, 150);
INSERT INTO stock VALUES(4, 4, null, 45);
INSERT INTO stock VALUES(5, 5, null, 78);
INSERT INTO stock VALUES(6, 6, null, 90);
INSERT INTO stock VALUES(7, 2, 1, 100);
INSERT INTO stock VALUES(8, 3, 2, 44);
INSERT INTO stock VALUES(9, 5, 3, 50);
INSERT INTO stock VALUES(10, 6, 4, 55);

INSERT INTO sell_offer VALUES(1, 1, 40, 58, 58, '2020-08-10', true);
INSERT INTO sell_offer VALUES(2, 2, 35, 70, 70, '2020-08-16', true);
INSERT INTO sell_offer VALUES(3, 3, 15, 80, 80, '2020-08-03', true);
INSERT INTO sell_offer VALUES(4, 4, 20, 10, 10, '2020-09-10', true);
INSERT INTO sell_offer VALUES(5, 5, 5, 100, 100, '2020-10-02', true);
INSERT INTO sell_offer VALUES(6, 6, 33, 23, 23, '2020-10-03', true);
INSERT INTO sell_offer VALUES(7, 8, 30, 40, 40, '2020-09-04', true);
INSERT INTO sell_offer VALUES(8, 9, 25, 50, 50, '2020-10-03', true);
INSERT INTO sell_offer VALUES(9, 7, 32, 52, 52, '2020-10-04', true);
INSERT INTO sell_offer VALUES(10, 10, 42, 45, 45, '2020-10-02', true);

INSERT INTO buy_offer VALUES(1, 1, 1, 50, 20, 20, '2020-08-23', true);
INSERT INTO buy_offer VALUES(2, 1, 2, 45, 13, 13, '2020-08-27', true);
INSERT INTO buy_offer VALUES(3, 2, 3, 10, 80, 80, '2020-08-02', true);
INSERT INTO buy_offer VALUES(4, 2, 4, 30, 46, 46, '2020-08-04', true);
INSERT INTO buy_offer VALUES(5, 3, 5, 35, 70, 70, '2020-08-05', true);
INSERT INTO buy_offer VALUES(6, 3, 6, 10, 34, 34, '2020-09-22', true);
INSERT INTO buy_offer VALUES(7, 4, 7, 28, 48, 48, '2020-09-16', true);
INSERT INTO buy_offer VALUES(8, 4, 8, 34, 7,  7,  '2020-10-15', true);
INSERT INTO buy_offer VALUES(9, 5, 9, 100,8,  78,  '2020-10-20', true);
INSERT INTO buy_offer VALUES(10, 6, 10, 29,8, 78, '2020-11-01', true);

INSERT INTO stock_rate VALUES(1, 1, 35, '2020-08-01', true);
INSERT INTO stock_rate VALUES(2, 2, 45, '2020-08-01', true);
INSERT INTO stock_rate VALUES(3, 3, 10, '2020-08-01', true);
INSERT INTO stock_rate VALUES(4, 4, 23, '2020-08-01', true);
INSERT INTO stock_rate VALUES(5, 5, 5, '2020-08-01', true);
INSERT INTO stock_rate VALUES(6, 6, 40, '2020-08-01', true);