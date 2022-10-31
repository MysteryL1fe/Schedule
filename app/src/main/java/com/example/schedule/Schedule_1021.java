package com.example.schedule;

public class Schedule_1021 extends Schedule {
    @Override
    public void Init() {
        Init_Monday_0();
        Init_Monday_1();
        Init_Tuesday_0();
        Init_Tuesday_1();
        Init_Wednesday_0();
        Init_Wednesday_1();
        Init_Thursday_0();
        Init_Thursday_1();
        Init_Friday_0();
        Init_Friday_1();
        Init_Saturday_0();
        Init_Saturday_1();
    }

    private void Init_Monday_1() {
        Monday_1[0] = "Ин.яз. доц. Вострикова И.Ю. 309П";
        Monday_1[1] = "Ин.яз. доц. Вострикова И.Ю. 309П";
        Monday_1[2] = "Теоретические основы инф. доц. Самодуров А.С. 387";
        Monday_1[3] = "Теоретические основы информатики доц. Самодуров А.С. 385";
    }

    private void Init_Monday_0() {
        Monday_0[0] = "Ин.яз. доц. Вострикова И.Ю. 309П";
    }

    private void Init_Tuesday_1() {
        Tuesday_1[1] = "Математический анализ доц. Сирота Е.А. 505П";
        Tuesday_1[2] = "История доц. Лавлинский С.А. 292";
        Tuesday_1[3] = "Математический анализ доц. Некрасова Н.Н. 380";
        Tuesday_1[4] = "Физическая культура и спорт";
        Tuesday_1[5] = "История доц. Лавлинский С.А. 477";
    }

    private void Init_Tuesday_0() {
        Tuesday_0[0] = "";
        Tuesday_0[1] = "Математический анализ доц. Сирота Е.А. 505П";
        Tuesday_0[2] = "История доц. Лавлинский С.А. 292";
        Tuesday_0[3] = "Математический анализ доц. Некрасова Н.Н. 380";
        Tuesday_0[4] = "Физическая культура и спорт";
        Tuesday_0[5] = "История доц. Лавлинский С.А. 477";
    }

    private void Init_Wednesday_1() {
        Wednesday_1[3] = "Экономика и финансовая грамотность доц. Павлова Е.А. 380";
        Wednesday_1[5] = "Введение в программирование ст.преп. Соломатин Д.И. 316П";
    }

    private void Init_Wednesday_0() {
        Wednesday_0[3] = "Экономика и финансовая грамотность доц. Павлова Е.А. 380";
        Wednesday_0[4] = "СПЭД ст.преп. Копытина Е.А. 293";
        Wednesday_0[5] = "Деловое общение и культура речи доц. Саломатина М.С. 479";
    }

    private void Init_Thursday_1() {
        Thursday_1[3] = "Деловое общение и культура речи доц. Саломатина М.С. 380";
        Thursday_1[4] = "Физическая культура и спорт";
    }

    private void Init_Thursday_0() {
        Thursday_0[3] = "Введение в прогр. ст.преп. Соломатин Д.И. 382";
        Thursday_0[4] = "Физическая культура и спорт";
    }

    private void Init_Friday_1() {
        Friday_1[0] = "Введение в программирование ст.преп. Соломатин Д.И. (ДО)";
        Friday_1[1] = "Теоретические основы информатики доц. Сычев А.В. (ДО)";
        Friday_1[2] = "Экономика и финансовая грамотность доц. Павлова Е.А. (ДО)";
    }

    private void Init_Friday_0() {
        Friday_0[0] = "Введение в программирование ст.преп. Соломатин Д.И. (ДО)";
        Friday_0[1] = "Теоретические основы информатики доц. Сычев А.В. (ДО)";
        Friday_0[2] = "Системы подготовки электронных документов ст.преп. Копытина Е.А. (ДО)";
    }

    private void Init_Saturday_1() {}

    private void Init_Saturday_0() {}
}
