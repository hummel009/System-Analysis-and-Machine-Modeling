[![Code Smells][code_smells_badge]][code_smells_link]
[![Maintainability Rating][maintainability_rating_badge]][maintainability_rating_link]
[![Security Rating][security_rating_badge]][security_rating_link]
[![Bugs][bugs_badge]][bugs_link]
[![Vulnerabilities][vulnerabilities_badge]][vulnerabilities_link]
[![Duplicated Lines (%)][duplicated_lines_density_badge]][duplicated_lines_density_link]
[![Reliability Rating][reliability_rating_badge]][reliability_rating_link]
[![Quality Gate Status][quality_gate_status_badge]][quality_gate_status_link]
[![Technical Debt][technical_debt_badge]][technical_debt_link]
[![Lines of Code][lines_of_code_badge]][lines_of_code_link]

Мои лабораторные работы для BSUIR/БГУИР (белорусский государственный университет информатики и радиоэлектроники).

Предмет - SAaMM/САиММод (системный анализ и машинное моделирование).

## Условия

### Лабораторная работа 1

Создать генератор последовательности равномерно распределенных случайных чисел на основе смешанного алгоритма Лемера.
Подобрать параметры генератора, который дает максимальный период.
Для сгенерированной выборки построить гистограмму (20+ интервалов).
Используя критерии хи-квадрат и КС, подтвердить гипотезу о равномерности последовательности. Отобразить результаты
графически.

### Лабораторная работа 2

Технологическая линия включает источник деталей двух типов, два параллельных станка для деталей каждого типа,
накопитель, транспортный работ, технологический модуль для окончательной сборки изделия, рабочее место комплектации
изделий, склад. Изделие состоит из трех деталей типа 1 и двух деталей типа 2. Детали со станка попадают в накопитель.
При образовании комплекта деталей, необходимых на изделие, транспортный робот перемещает их в технологический модуль.
Изделия после сборки комплектуются в партии по 8 штук. Транспортный робот отправляет по 3 партии на склад.

### Лабораторная работа 3

Для имитационной модели сложной системы согласно ЛР2 решить следующие задачи:

* проверить гипотезу о нормальности распределения откликов;
* вычислить точечные и интервальные оценки откликов ИМ в опыте из 10 прогонов при уровне значимости 0.05;
* оценить зависимость точности имитации от количества прогонов;
* оценить чувствительность откликов к вариациям переменных ИМ;
* построить зависимость изменения какого-либо отклика в модельном времени, выдвинуть и проверить гипотезу об уменьшении
  времени прогона, исключая переходный период;
* проверить гипотезу о возможности постановки опыта с непрерывным прогоном.

Отобразить результаты решения задач с помощью диаграмм.

### Лабораторная работа 4

Для имитационной модели сложной системы согласно ЛР2 решить следующие задачи:

* построить зависимость отклика от варьирования параметра модели на 7+ уровнях, выполнить линейную
  и нелинейную (любую) аппроксимацию, сделать вывод о наилучшем приближении, учитывая погрешность имитации;
* реализовать эксперимент по сравнению трёх альтернатив использования объекта моделирования;
* поставить двухфакторный эксперимент (4+ уровня для каждого фактора) и отобразить поверхность отклика.

Отобразить результаты решения задач с помощью диаграмм.

<!----------------------------------------------------------------------------->

[code_smells_badge]: https://sonarcloud.io/api/project_badges/measure?project=hummel009_System-Analysis-and-Machine-Modeling&metric=code_smells

[code_smells_link]: https://sonarcloud.io/summary/overall?id=hummel009_System-Analysis-and-Machine-Modeling

[maintainability_rating_badge]: https://sonarcloud.io/api/project_badges/measure?project=hummel009_System-Analysis-and-Machine-Modeling&metric=sqale_rating

[maintainability_rating_link]: https://sonarcloud.io/summary/overall?id=hummel009_System-Analysis-and-Machine-Modeling

[security_rating_badge]: https://sonarcloud.io/api/project_badges/measure?project=hummel009_System-Analysis-and-Machine-Modeling&metric=security_rating

[security_rating_link]: https://sonarcloud.io/summary/overall?id=hummel009_System-Analysis-and-Machine-Modeling

[bugs_badge]: https://sonarcloud.io/api/project_badges/measure?project=hummel009_System-Analysis-and-Machine-Modeling&metric=bugs

[bugs_link]: https://sonarcloud.io/summary/overall?id=hummel009_System-Analysis-and-Machine-Modeling

[vulnerabilities_badge]: https://sonarcloud.io/api/project_badges/measure?project=hummel009_System-Analysis-and-Machine-Modeling&metric=vulnerabilities

[vulnerabilities_link]: https://sonarcloud.io/summary/overall?id=hummel009_System-Analysis-and-Machine-Modeling

[duplicated_lines_density_badge]: https://sonarcloud.io/api/project_badges/measure?project=hummel009_System-Analysis-and-Machine-Modeling&metric=duplicated_lines_density

[duplicated_lines_density_link]: https://sonarcloud.io/summary/overall?id=hummel009_System-Analysis-and-Machine-Modeling

[reliability_rating_badge]: https://sonarcloud.io/api/project_badges/measure?project=hummel009_System-Analysis-and-Machine-Modeling&metric=reliability_rating

[reliability_rating_link]: https://sonarcloud.io/summary/overall?id=hummel009_System-Analysis-and-Machine-Modeling

[quality_gate_status_badge]: https://sonarcloud.io/api/project_badges/measure?project=hummel009_System-Analysis-and-Machine-Modeling&metric=alert_status

[quality_gate_status_link]: https://sonarcloud.io/summary/overall?id=hummel009_System-Analysis-and-Machine-Modeling

[technical_debt_badge]: https://sonarcloud.io/api/project_badges/measure?project=hummel009_System-Analysis-and-Machine-Modeling&metric=sqale_index

[technical_debt_link]: https://sonarcloud.io/summary/overall?id=hummel009_System-Analysis-and-Machine-Modeling

[lines_of_code_badge]: https://sonarcloud.io/api/project_badges/measure?project=hummel009_System-Analysis-and-Machine-Modeling&metric=ncloc

[lines_of_code_link]: https://sonarcloud.io/summary/overall?id=hummel009_System-Analysis-and-Machine-Modeling
