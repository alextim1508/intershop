INSERT INTO user_details(username, password, roles, created) values ('admin', '$2a$10$sDqj8YbK78ZdBcUU1O6/o.BSep4OkUkNcMnAXTsyd/FT.I.9IOr5a', '["ADMIN_ROLE"]', CURRENT_DATE) ON CONFLICT DO NOTHING;
INSERT INTO user_details(username, password, roles, created) values ('moder', '$2a$10$sDqj8YbK78ZdBcUU1O6/o.BSep4OkUkNcMnAXTsyd/FT.I.9IOr5a', '["MODERATOR_ROLE", "USER_ROLE"]', CURRENT_DATE) ON CONFLICT DO NOTHING;
INSERT INTO user_details(username, password, roles, created) values ('alex', '$2a$10$sDqj8YbK78ZdBcUU1O6/o.BSep4OkUkNcMnAXTsyd/FT.I.9IOr5a', '["USER_ROLE"]', CURRENT_DATE) ON CONFLICT DO NOTHING;

INSERT INTO items (title, description, img_path, price) VALUES (
'Перчатки ММА BoyBo Stain Black',
'Стильные черные перчатки для ММА от компании BoyBo надежно защищают кисти от травм, имеют идеальную посадку. Изделие изготовлено из прочных, качественных материалов, которые сохраняют аккуратную форму и внешний вид после длительного использования.Для амортизации ударов и защиты кисти от травм использован амортизирующий слой пены. Изделие достаточно легкое, практически не ощущается на руке, а благодаря качественным липучкам отлично фиксируется, не вызывая дискомфорт.Практичный и лаконичный дизайн модели Stain Black универсальный, подойдет всем без исключения.',
'https://спортфайтер.рф/images/stories/virtuemart/product/perchatki-mma-boybo-stain-black6.jpg',
1990);


INSERT INTO items (title, description, img_path, price) VALUES (
'Перчатки для ММА Green Hill - красные',
'Перчатки Green Hill для занятий различными видами единоборств.Перчатки удобно сидят на руке, благодаря открытой ладони удобно применять захваты.Изготовлены перчати из высококачественной кожи, что обеспечит долгий срок службы перчаток, а внутренний наполнитель из плотной пены обеспечит хорошую защиту вашим кистям, тем самым убережет ваши руки от различных травм.',
'https://спортфайтер.рф/images/stories/virtuemart/product/perchatki-dlya-mma-green-hill---krasnye5.jpg',
2800);


INSERT INTO items (title, description, img_path, price) VALUES (
'Перчатки для MMA Fight Expert черные GGKM354',
'Перчатки оснащены наполнителем из пенополиуретана, который эффективно поглощает удары, минимизируя риск травм для спортсмена и его противника. Этот материал гарантирует отличную амортизацию и распределение ударной нагрузки. Широкий ремешок с липучкой гарантирует надежную фиксацию перчаток на руке, предотвращая их смещение во время интенсивных тренировок и боев. Эти элементы позволяют легко настраивать плотность прилегания, что повышает удобство и поддержку запястья. Интегрированная защита большого пальца предотвращает его травмы и защищает от потенциальных вывихов, что особенно важно в боевых ситуациях и при выполнении бросков и захватов. Ладонь перчаток выполнена из сетчатого материала, который способствует отличной вентиляции и снижает потоотделение.',
'https://спортфайтер.рф/images/stories/virtuemart/product/perchatki-dlya-mma-fight-expert-chernye-ggkm354-(2).jpg',
2875);


INSERT INTO items (title, description, img_path, price) VALUES (
'Перчатки боевые Союз ММА России синие',
'Перчатки боевые Союз ММА России синего цвета. Изготовлены эксклюзивно для Союза ММА. Особое внимание уделено безопасности. Большой палец дополнительно защищен, но в то же время это не мешает при борьбе. Очень удобно надеваются. Манжет перчатки оснащен двойной липучкой. Выполнены из качественных синтетических материалов.',
'https://спортфайтер.рф/images/stories/virtuemart/product/perchatki-boevye-soyuz-mma-rossii-sinie-56.jpg', 4290);


INSERT INTO items (title, description, img_path, price) VALUES (
'Перчатки для ММА Venum Rumble',
'Изготовлены из качественной синтетической кожи Skintex, перчатки Venum обладают прочностью и долговечностью, обеспечивая надежную защиту рук во время интенсивных тренировок. Защита большого пальца предотвращает возможные травмы этого уязвимого участка руки. Длинная манжета с липучкой гарантирует надежную посадку и поддержку запястья, предотвращая травмы и обеспечивая стабильность. Особенностью перчаток является внутренняя подкладка из неопрена, которая способствует дополнительному комфорту и эффективно усваивает влагу. Благодаря своим характеристикам, перчатки Venum Rumble представляют собой надежное и удобное снаряжение для занятий ММА, обеспечивая безопасность рук, комфорт и высокую производительность во время тренировок и поединков.',
'https://спортфайтер.рф/images/stories/virtuemart/product/perchatki-dlya-mma-venum-rumble-(2).jpg', 4690);


INSERT INTO items (title, description, img_path, price) VALUES (
'Шлем боксёрский открытый BoyBo Flex Red',
'Открытый боксерский шлем BoyBo Flex Red обладает отличными характеристиками и надежно защищает от травм. Изделие оснащено наполнителем из плотной пены, которая эффективно гасит удары, обладая хорошими амортизирующими свойствами. Изделие фиксируется на голове и регулируется по размеру липучками, расположенными в зоне подбородка и на затылке, а также веревкой, которая расположена в теменной части. Шлем выполнен из качественной синтетической кожи. Материал изделия легкий, но прочный, выдерживает высокие нагрузки.',
'https://спортфайтер.рф/images/stories/virtuemart/product/shlem-boksjorskij-otkrytyj-boybo-flex-red2.jpg',
2190 );



INSERT INTO items (title, description, img_path, price) VALUES (
'Шлем боксёрский открытый BoyBo Leather Red',
'Шлем боксёрский открытый BoyBo данный шлем – открытого типа (отсутствует защита темени головы). Для защиты ушей спортсмена от воздушных ударов предусмотрена пористая вставка в ушной части снаряжения. изготовлен из качественной натуральной кожи. В качестве наполнителя используется упругий пенистый материал, отличающийся способностью лучше поглощать удары.',
'https://спортфайтер.рф/images/stories/virtuemart/product/shlem-boksjorskij-otkrytyj-boybo-leather-red5.jpg',
10650 );


INSERT INTO items (title, description, img_path, price) VALUES (
'Шлем Hayabusa T3 Black/Gold',
'Шлем Hayabusa T3 Black изготовлен из кожи Vylar - кожа последнего поколения, которая в ходе проведенных испытаний показала выносливость и ударостойкость. Шлем испытывался на предмет сдерживания максимально сильных ударов. Так же данный шлем обеспечивает ещё больший комфорт и широкий угол обзора для бойца. Шлем дышит и не позволяет влаге скапливаться в районе головы. Закрывает слабое место – ухо бойца!',
'https://спортфайтер.рф/images/stories/virtuemart/product/shlem-hayabusa-t3-black-gold7.jpg',
28970 );


INSERT INTO items (title, description, img_path, price) VALUES (
'Шлем боксерский LEADERS LS с бампером зеленый', 'Это высококачественный шлем, изготовленный из 100% коровьей кожи премиум-класса. Он имеет яркий салатовый цвет, который привлекает внимание.
Шлем оснащен удобной системой регулировки. С помощью шнуровки сверху и на затылочной части можно настроить подходящую посадку для каждого пользователя. Подбородок фиксируется с помощью клипсы, обеспечивая надежную защиту и комфорт при использовании. Благодаря бамперной защите, он обеспечивает дополнительную безопасность, смягчая и поглощая ударную энергию. Он идеально подходит для тренировок и поединков в боксе или других единоборствах.',
'https://спортфайтер.рф/images/stories/virtuemart/product/shlem-bokserskij-leaders-ls-s-bamperom-zelenyj5.jpg',
14900 );

INSERT INTO items (title, description, img_path, price) VALUES (
'Шлем боксерский LEADERS LS с бампером черный',
'Изделие произведено из мягкой натуральной кожи, которая не деформируется в процессе использования. Внутренняя подкладка выполнена из специального материала. Он отлично впитывает влагу и не вызывает скольжения. Шлем дает спортсмену широкий угол обзора. Форма изделия обеспечивает максимальную защиту лица и головы. Набивка эффективно амортизирует мощные удары и оберегает бойца от различного рода травм. Комфортная и плотная посадка регулируется шнуровкой, которая расположена сверху и на затылке. В области подбородка находится клипса. В передней части изделия есть фирменный логотип.',
'https://спортфайтер.рф/images/stories/virtuemart/product/shlem-bokserskij-leaders-ls-s-bamperom-chernyj5.jpg',
11990 );

INSERT INTO items (title, description, img_path, price) VALUES (
'Боксерки Venum Contender White/Grey',
'Эти боксерки впечатляют своей легкостью и высокой адаптивностью. Мягкий, но прочный верх выполнен из дышащей сетки, которая гарантирует оптимальную вентиляцию во время тренировочных сессий. Эргономичная подошва — это одно из главных преимуществ Venum Contender. Она разработана для обеспечения надежного сцепления с любыми типами поверхностей, будь то тренировочная площадка или ринг. Особое внимание уделено устойчивости и распределению нагрузки, что снижает риск скольжения и травм. Голеностопная часть ботинок обеспечена дополнительной поддержкой, что помогает спортсменам сохранять уверенность в каждом движении. Средняя высота модели идеально фиксирует ногу, не ограничивая свободу движений, что особенно важно для работы ног и скорости маневров.',
'https://спортфайтер.рф/images/stories/virtuemart/product/bokserki-venum-contender-whitegrey-(8).jpg',
14590 );


INSERT INTO items (title, description, img_path, price) VALUES (
'Боксёрки Nike Machomai 2.0 White/Black',
'Конструкция боксерок отличается легкостью и удобством. Верх выполнен из сочетания синтетической кожи и воздухопроницаемой сетки, что обеспечивает надежность и вентиляцию. Подошва боксерок спроектирована для оптимального сцепления с рингом. Изготовленная из износостойкой резины, она обладает специальным рифлением, которое помогает спортсмену уверенно двигаться и сохранять баланс. Тонкая подошва позволяет чувствовать поверхность ринга, улучшая контроль над каждым шагом и движением. Средняя высота обуви обеспечивает поддержку голеностопа, снижая риск травм и растяжений. Шнуровка позволяет точно подогнать боксерки под форму ноги, создавая плотную и надежную посадку, которая не допускает смещения обуви даже при самых активных движениях.',
'https://спортфайтер.рф/images/stories/virtuemart/product/boksjorki-nike-machomai-2.0-whiteblack-(8).jpg',
21970);



INSERT INTO items (title, description, img_path, price) VALUES (
'Боксерки Clinch Olimp 2.0 сине-красные',
'Жесткий задник обуви эффективно фиксирует пятку и голеностопный сустав, обеспечивая стабильность и защиту от травм во время быстрых движений и поворотов. Шнуровка позволяет регулировать посадку, достигая оптимального уровня поддержки и комфорта. Состав обуви состоит из 60% текстиля и 40% полиуретана, что обеспечивает сочетание прочности и легкости. Подошва из пенного материала с противоскользящим протектором гарантирует отличное сцепление с поверхностью ринга или зала, предотвращая скольжение и обеспечивая устойчивость во время движений. Высокое голенище способствует устойчивости стопы при быстрых маневрах и ударах. Это особенно важно для боксеров, которые стремятся к максимальной производительности и защите во время тренировок и соревнований.',
'https://спортфайтер.рф/images/stories/virtuemart/product/bokserki-clinch-olimp-2.0-sine-krasnye-4.jpg',
7470 );


INSERT INTO items (title, description, img_path, price) VALUES (
'Боксёрки Сабо Ring Master Black/Grey',
'Стильные боксерки в черно-сером цвете от фирмы Сабо. Обеспечивают комфорт и уверенность на ринге. Модель пошита из качественных, прочных материалов, способных выдерживать сильные нагрузки. Боксерки средней высоты, они надежно фиксируются на ноге при помощи шнуровки. Пошиты из текстиля AirMesh в сочетании с сеткой, для подошвы производитель использовал полимер. Подошва цельная и нескользящая, что очень важно для тренировок и спаррингов по боксу. Это обувь, в которой боксерам обеспечен комфорт на ринге, а также уверенность в защите ног и стопы во время тренировок.',
'https://спортфайтер.рф/images/stories/virtuemart/product/boksjorki-sabo-ring-master-black-grey.jpg',
4990 );


INSERT INTO items (title, description, img_path, price) VALUES (
'Боксерки Fight Expert FX Boxing 2.0',
'Для тренировок на боксерском ринге идеально подойдет новая модель боксерок Fx Boxing 2.0. Они могут помочь вам прийти к достижению результатов профессионального боксера. Модель первоклассного качества, представлены в универсальном черном цвете. Они имеют большое количество преимуществ, включая износостойкий и дышащий материал, для дополнительной защиты и комфорта. Дизайн боксерок Fx Boxing 2.0 достаточно практичный, они удобно сидят на ногах, обеспечивая необходимый комфорт во время тренировок и спаррингов. Сетчатые вставки позволяют коже дышать.',
'https://спортфайтер.рф/images/stories/virtuemart/product/bokserki-fight-expert-fx-boxing-2.01.jpg',
 5670 );

INSERT INTO items (title, description, img_path, price) VALUES (
'Рашгард Hardcore Training Ta Moko Black',
'Рашгард от известной компании Hardcore Training модель Ta Moko Black изготовлен только из качественных материалов. Он отличается от других спортивных вещей тем, что быстро впитывает всю влагу и очень быстро сохнет. Благодаря этому неприятного запаха не будет. Также, такая вещь сохраняет температуру тела спортсмена, что защищает его от различных травм во время тренировок.Длинные рукава рашгарда уберегут кожу от счесывания о маты. Ткань плотно сидит на теле, приятная на ощупь и не вызывает раздражения. Прочные швы не натирают кожу. Удобство и комфорт во время тренировок Вам обеспечен!',
'https://спортфайтер.рф/images/stories/virtuemart/product/rashgard-hardcore-training-ta-moko-black6.jpg',
6800 );


INSERT INTO items (title, description, img_path, price) VALUES (
'Рашгард Hardcore Training Viking дл.рукав',
'Материал рашгарда обладает высокой воздухопроницаемостью, что позволяет коже дышать даже при интенсивных нагрузках. Ткань эффективно отводит влагу от тела, предотвращая перегрев и обеспечивая комфорт на протяжении всей тренировки. Особая структура ткани способствует сохранению оптимальной температуры тела. Это особенно важно во время разминки и основной тренировки, когда мышцы должны оставаться разогретыми для максимальной эффективности движений и предотвращения травм. Плоские швы выполнены таким образом, чтобы не вызывать дискомфорт или раздражение кожи. Дополнительным преимуществом является защита кожного покрова. Плотная, но легкая ткань предотвращает мелкие травмы, порезы и ссадины, которые могут возникнуть во время схваток и тренировок. Кроме того, рашгард защищает от вредного воздействия ультрафиолетовых лучей при тренировках на открытом воздухе.',
'https://спортфайтер.рф/images/stories/virtuemart/product/rashgard-hardcore-training-viking-ls.jpg', 6890 );


INSERT INTO items (title, description, img_path, price) VALUES (
'Рашгард Hardcore Training Koi 2.0',
'Шикарная новинка на самурайскую тему от НСТ. Сдержанный черно-белый дизайн, отличная графика и сплошной дзен. Все краски сублимированны в ткань, не сотрутся и не потускнеют со временем! Качественные плоские швы не натирают кожу. Длинный рукав уберегает кожу рук при работе в партере от травмирования об жесткие маты и татами. Ткань очень приятная на ощупь, поэтому удобно сидит на теле и не раздражает кожу.',
'https://спортфайтер.рф/images/stories/virtuemart/product/rashgard-hardcore-training-koi-2.0-3.jpg',
6470 );

INSERT INTO items (title, description, img_path, price) VALUES (
'Рашгард с длинным рукавом FIXGEAR wolf',
'Рашгард с длинным рукавом FIXGEAR приятный материал, хорошо тянется, защитит вас при работе в партере и стойке + сохранит тело в разогретом состояние. Очень быстро высыхает, а это значит у вас не будит дискомфорта как при тренировке в обычной футболке которая прилипает к телу. Подойдет для любого спорта: единоборства, тренажерный зал, футбол и др.',
'https://спортфайтер.рф/images/stories/virtuemart/product/rashgard-s-dlinnym-rukavom-fixgear-wolf.jpg',
4950 );


INSERT INTO items (title, description, img_path, price) VALUES (
'Рашгард BTOPerform - Camo-Urban',
'Рашгард с длинным рукавом BTOPerform - камуфлированный.Материал очень хорошо тянется поэтому рашгард сядет по фигуре как нужно.Также благодаря хорошей эластичности рашгард очень удобный.Сохраняет оптимальную температуру тела, поэтому во время отдыха на тренировке тело не будет остывать, что минимизирует риск получения травм.Впитывает влагу и быстро сохнет, а также защищает вашу кожу от ожогов о маты.Рисунок полностью сублимирован в ткань, поэтому он никогда не сотрется.',
 'https://спортфайтер.рф/images/stories/virtuemart/product/rashgard-btoperform---kamuflyazh2.jpg',
4190 );


INSERT INTO items (title, description, img_path, price) VALUES (
'Шорты для ММА детские Rusco Sport Cerberus',
'Тренироваться в специально предназначенной для этого одежде значительно комфортней. Шорты для ММА пошиты из легкого полиэстера, который отлично впитывает влагу и быстро высыхает, что важно при интенсивных тренировках. Представленная модель шорт имеет разрезы с двух сторон, что помогает спортсменам выполнять все упражнения без ограничений. На внутренней стороне бедра предусмотрена эластичная вставка для комфорта спортсмена во время тренировок.',
 'https://спортфайтер.рф/images/stories/virtuemart/product/shorty-dlya-mma-detskie-rusco-sport-cerberus-73.jpg', 1950 );


INSERT INTO items (title, description, img_path, price) VALUES (
'Шорты для MMA BoyBo Animals',
'Изготовленные из 100% полиэстера, шорты BoyBo обеспечивают идеальный баланс между легкостью и долговечностью. Благодаря уникальному сочетанию материалов эти шорты не только выдерживают суровые условия тренировок MMA, но и обеспечивают комфорт, повышающий концентрацию внимания и работоспособность. Конструкция шорт тщательно продумана и учитывает каждое ваше движение. Эластичный пояс в сочетании с практичным утягивающим шнурком обеспечивает надежную и индивидуальную посадку на талии. Это означает, что вы можете полностью сосредоточиться на тренировке, не прибегая к постоянной подгонке шорт. Кроме того, наличие эластичной вставки между ног и продуманных разрезов по бокам повышает свободу движений. Рисунок на этих шортах сублимирован непосредственно в ткань. Эта передовая технология не только обеспечивает яркие и стойкие цвета, но и гарантирует сохранение рисунка даже после бесчисленных стирок.',
'https://спортфайтер.рф/images/stories/virtuemart/product/shorty-dlya-mma-boybo-animals-detskie.jpg', 2790 );


INSERT INTO items (title, description, img_path, price) VALUES (
'Шорты для ММА SixF Медведь',
'Шорты для ММА от 6fwear выполнены из синтетической ткани, в состав которой входит полиэстер. Материал долговечный и износостойкий, не требует специального ухода, выглядит аккуратно после десятков стирок. Ткань очень легкая, но выдерживает высокие нагрузки.Благодаря правильному крою шорты не сковывают движения спортсмена, позволяют с комфортом отрабатывать различные приемы и техники. По бокам изделия и на внутренней стороне бедра есть эластичные вставки. Модель подходит для тренировок в зале или на улице, предназначена не только для единоборств, но и для фитнеса, бега и других видов спорта.',
'https://спортфайтер.рф/images/stories/virtuemart/product/333121.jpg', 3990 );


INSERT INTO items (title, description, img_path, price) VALUES (
'Шорты для ММА Hardcore Training Koi',
'Шорты для смешанных единоборств от Hardcore Training!Прочный полиэстер, качественная строчка, делают эти шорты очень износостойкими.Удобную посадку обеспечивает тянущейся материал, разрезы по бокам и дополнительная эластичная вставка в районе промежности.Фиксируются на поясе при помощи удобной липучки и внутреннего шнурка.Шорты черного цвета с рисунком в самурайской тематике.Рисунок сублимирован в ткань - это значит, что он не сотрется и не потускнеет.',
'https://спортфайтер.рф/images/stories/virtuemart/product/shorty-hardcore-training-koi.jpg',
7490);


INSERT INTO items (title, description, img_path, price) VALUES (
'Шорты ММА Venum Technical 3.0 Forest Green',
'Изготовленные из высококачественного и износостойкого материала, шорты обладают отличной вентиляцией, что предотвращает перегрев во время интенсивных нагрузок. Легкая ткань не сковывает движения и быстро высыхает, что особенно важно для спортсменов, подвергающихся большим физическим нагрузкам. Удлиненный крой и боковые разрезы обеспечивают дополнительную мобильность и удобство при выполнении резких движений, ударов и бросков. Это особенно важно для бойцов, которым нужна максимальная амплитуда движений при работе в стойке и партере. Эластичный пояс с надежным шнурком гарантирует отличную фиксацию на теле, предотвращая сползание. Дизайн шорт выполнен в темно-зеленом оттенке с контрастными элементами, что придает им стильный и современный вид. Сублимированные узоры, нанесенные с применением передовых технологий, устойчивы к истиранию и сохраняют насыщенность цветов.',
'https://спортфайтер.рф/images/stories/virtuemart/product/shorty-mma-venum-technical-3.0-forest-green-(3).jpg',
9840 );

INSERT INTO items (title, description, img_path, price) VALUES (
'Скоростная груша RDX Multi бело-красная',
'Изготовленная из натуральной воловьей кожи, эта груша не только отличается исключительной прочностью, но и легко чистится, сохраняя эстетичный внешний вид и эксплуатационные характеристики на протяжении долгого времени. Стальное крепление с системой B-Balancing™ обеспечивает устойчивость и сбалансированность. На каждый удар она отвечает чутким отскоком, повышая вашу концентрацию, скорость и точность.',
'https://спортфайтер.рф/images/stories/virtuemart/product/rdx_s2_boxing_training_speed_bag_5_.jpg',
 6490 );


INSERT INTO items (title, description, img_path, price) VALUES (
'Груша скоростная Lonsdale Bullet 23*15см черн',
'Размеры груши составляют 23х15 см, что делает её идеальной для работы на высоких скоростях и оттачивания точности удара. За счет небольшой площади поверхности спортсмену необходимо постоянно контролировать положение рук и интенсивность нанесения ударов, тем самым повышая технику исполнения и скорость реакции на движения цели. Материал груши – высококачественная искусственная кожа, обладающая хорошей прочностью, долговечностью и устойчивостью к интенсивным нагрузкам. Дизайн изделия выполнен в классическом стиле бренда Lonsdale London, известного своими спортивными аксессуарами премиум-класса. Груша окрашена в стильный черный цвет с фирменными элементами брендинга, что гармонично дополнит интерьер любого спортивного зала или домашнего тренировочного пространства.',
'https://спортфайтер.рф/images/stories/virtuemart/product/grusha-skorostnaya-lonsdale-bullet-(7).jpg',
3440 );



INSERT INTO items (title, description, img_path, price) VALUES (
'Груша скоростная Lonsdale Pace 25*18см сер.',
 'Изготовленная из качественной искусственной кожи, груша отличается повышенной устойчивостью к механическим повреждениям и износу. Материал не только долговечен, но и приятен на ощупь, что значительно повышает комфорт при работе со снарядом. Конструкция груши способствует быстрому и точному отскоку, что эффективно тренирует не только скорость и координацию движений, но и реакцию спортсмена. Стильный спортивный дизайн груши с узнаваемым логотипом бренда Lonsdale будет гармонично смотреться в любом тренировочном зале или домашнем спортивном уголке. Lonsdale – это бренд, который ассоциируется с качеством и надёжностью среди профессионалов бокса по всему миру. Скоростная груша Pace станет отличным дополнением к арсеналу спортсмена, стремящегося к совершенству в боксе и единоборствах.',
'https://спортфайтер.рф/images/stories/virtuemart/product/grusha-skorostnaya-lonsdale-pac-(1).jpg',
3990 );



INSERT INTO items (title, description, img_path, price) VALUES (
'Лапы боксерские LEADERS Big-Air',
'Изделие предназначено для обеспечения защиты рук тренера в ходе отработки ударной техники. Универсальная форма имеет небольшой вес и снижает усиленную нагрузку на плечевой сустав. Лапы выполнены из натуральной кожи, характеризующейся повышенными износостойкими свойствами. Для дополнительной защиты пальцев служит закрывающий карман, имеющий тонкий пенный слой. Для поглощения силы ударов внутреннее пространство заполнено скомпонованной высокотехнологичной пеной. Удобную посадку и плотную фиксацию обеспечивает расположенная застежка на запястье, которая крепится на липучку.',
'https://спортфайтер.рф/images/stories/virtuemart/product/lapy-bokserskie-leaders-big-air5.jpg',
12690 );



INSERT INTO items (title, description, img_path, price) VALUES (
'Лапы боксерские LEADERS MID AIR Red Alert',
'Стильные, удобные и качественные боксерские лапы для эффективных тренировок. Представленная модель пошита из прочных материалов, что позволяет выдерживать высокоинтенсивные нагрузки в течение длительного периода времени. Модель выполнена в стильном, ярком красном оттенке, дополнена логотипом бренда LEADERS.Лапы прочно фиксируются на кисти рук тренера, и позволяют боксерам отрабатывать точность и скорость ударов, что очень важно для эффективных тренировок и улучшения спортивных результатов. Модель представлена классической круглой формы и стандартного размера',
'https://спортфайтер.рф/images/stories/virtuemart/product/lapy-bokserskie-leaders-mid-air-red-alert1.jpg',
11690 );

INSERT INTO items (title, description, img_path, price) VALUES (
'Тренерские палки LEADERS Boxing Punch Sticks - зел.',
'Парная модель изготовлена из натуральной кожи. Предназначена для тренировочного процесса, когда необходимо отработать ударную технику и защиту. Позволяет улучшить скоростной режим и внимательность. Изделие характеризуется упругостью. Во внутренней его части расположен пластиковый сердечник, чтобы сохранять первоначальную форму модели. Для удобства хвата предусмотрены петли. Их использование позволяет крепко удерживать спортивный инструмент без его выпадения во время ударов с большой силой. Яркий цвет делает модель заметной и лучше тренирует концентрацию внимания',
'https://спортфайтер.рф/images/stories/virtuemart/product/trenerskie_palki_leaders_boxing_punch_sticks_gr_trenerskije_palki_leaders_boxing_punch_sticks_gr_186.jpg',
5800 );

INSERT INTO items (title, description, img_path, price) VALUES (
'Тренерские палки SP черно-красные',
'Специальные тренерские палки используются на тренировках для повышения их эффективности. Они отлично развивают скорость реакции боксеров, также с их помощью отрабатывают защитные удары и атакующие. Использование тренерских палок уменьшает травматизм тренера, при этом улучшаются результаты спортсменов. Представленная модель изготовлена в стильном черно-красном цвете, и дополнены нанесенным логотипом бренда в белом цвете. Тренерские палки из качественного материала, искусственная кожа премиального качества. Они способны выдерживать высокоинтенсивные нагрузки в течение длительного времени.',
'https://спортфайтер.рф/images/stories/virtuemart/product/trenerskie-palki-sp-cherno-krasnye.jpg',
 2390 );

INSERT INTO items (title, description, img_path, price) VALUES (
'Капа боксерская Venum Predator Black/Black',
'Капа Venum Predator Black/Black – это пример качественной и максимально комфортной защиты для зубов. Капа идеально садится, принимая форму зубов спортсмена, она гибкая, не затрудняет дыхание, и надежно защищает челюсть во время боя.Особая конструкция изделия обеспечивает интеллектуальную защиту челюсти от удара: капа сначала поглощает самую мощную часть удара, а затем рассеивает ударную волну по направлению к крепким участкам челюсти.',
'https://спортфайтер.рф/images/stories/virtuemart/product/kapa-bokserskaya-venum-predator-black-black-2.jpg',
 2350  );


INSERT INTO items (title, description, img_path, price) VALUES (
'Капа мятная Paffen-Sport черная',
'Занимаясь различными единоборствами человек должен понимать, что без такого изделия как капа просто не обойтись. Желая защитить себя и свое здоровье во время спаррингов, боец может приобрести капу Паффен спорт черного цвета. Эта капа имеет мятный запах, она изготовлена из термопластика высокого качества, хорошо заваривается. Для хранения капы в комплекте идет специальный контейнер.',
'https://спортфайтер.рф/images/stories/virtuemart/product/kapa-myatnaya-paffen-sport-chernaya.jpg',
2350 );


INSERT INTO items (title, description, img_path, price) VALUES (
'Пояс тренера KING - чёрный',
'Пояс тренера KING, изготовлен вручную в Таиланде из высококачественной кожи. Имеет дополнительные усиленные боковые зоны для удара. Удобная и плотная посадка. Внутренний наполнитель плотная пена, которая отлично поглощает удары и тем самым уберегая от травм. Фиксация за спиной с помощью широкой липучки, что позволяет без помощи одеть его на пояс.',
'https://спортфайтер.рф/images/stories/virtuemart/product/poyas-trenera-king---chjornyj6.jpg',
15550 );

INSERT INTO items (title, description, img_path, price) VALUES (
'Тренерский жилет Leaders черно-зеленый',
'Внутри жилет оснащен многослойной набивкой из пены разной плотности, которая имеет толщину 12 см. Такая конструкция эффективно гасит даже самые мощные удары, обеспечивая защиту тренера от травм и снижая нагрузку на его тело. Внутренние слои пены специально разработаны для того, чтобы распределять силу удара по всей площади жилета, минимизируя точечное давление. Особое внимание уделено удобству ношения: жилет оснащен регулируемыми ремнями, которые позволяют легко и надежно подогнать его по фигуре. Это обеспечивает плотное его прилегание к телу, исключая его смещение во время тренировочного процесса. Легкость жилета — ещё одно его преимущество. Несмотря на толстую набивку и прочную конструкцию, он остаётся чрезвычайно легким, не ограничивая движения тренера и не вызывая усталости даже при длительных тренировках. Таким образом, этот тренерский жилет идеально подходит для профессиональных тренеров, которые работают с боксерами любого уровня, обеспечивая максимальную защиту, комфорт и долговечность.',
'https://спортфайтер.рф/images/stories/virtuemart/product/trenerskij-zhilet-leaders-cherno-zelenyj-(2).jpg',
25990 );


INSERT INTO items (title, description, img_path, price) VALUES (
'Защита ног Reyvel Maximum Protection черно-белая',
'Во время тренировок, спортсменам важно обеспечить безопасность всех частей тела, и в том числе ног. Для этого используется специальная защита для голеностопа, которая плотно прилегает к ноге, фиксируется при помощи манжет-липучек, и обеспечивает смягчение ударов. Защита для голени и стопы от бренда Reyvel выполнена из прочной искусственной кожи, которая стойкая к ударам. Дополнительно для защиты голени и стопы используется трехслойный вкладыш из полимерной пены. Защита легкая и не мешает эффективно тренироваться и выполнять удары.',
'https://спортфайтер.рф/images/stories/virtuemart/product/zashchita-nog-reyvel-maximum-protection-cherno-belaya3.jpg',
 7200 );


INSERT INTO items (title, description, img_path, price) VALUES (
'Защита ног Danata Premier Black',
'Модель подходит для тренировочного процесса. Выдерживает повышенные силовые нагрузки при ударах ногами. Изделие изготовлено из натуральной кожи, характеризующейся высоким качеством. Максимальная защита обеспечивается за счет плотного прилегания к ноге и стопе, без смещений во время активных действий. Это возможно за счет расположенных в верхней и нижней части изделия застежках на липуче, которые закрываются на оборот через петли. Привлекательный внешний вид модели подходит к любой форме. Нанесение стойкое, устойчиво к истиранию на протяжение длительного срока эксплуатации',
'https://спортфайтер.рф/images/stories/virtuemart/product/zashchita-nog-danata-pemier-black9.jpg',
4790 );
