package com.example.schedule;

public class Schedule_1062 extends Schedule{
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
        Monday_1[4] = "Теоретические основы информатики доц. Самодуров А.С. 385";
    }

    private void Init_Monday_0() {
        Monday_0[4] = "Ин.яз. преп. Гавриш О.А. 308П";
        Monday_0[5] = "Теоретические основы информатики доц. Самодуров А.С. 301П";
    }

    private void Init_Tuesday_1() {
        Tuesday_1[4] = "Физическая культура и спорт";
        Tuesday_1[5] = "Математический анализ доц. Сирота Е.А. 479";
    }

    private void Init_Tuesday_0() {
        Tuesday_0[4] = "Физическая культура и спорт";
        Tuesday_0[5] = "Математический анализ доц. Сирота Е.А. 479";
    }

    private void Init_Wednesday_1() {
        Wednesday_1[1] = "Экономика и финансовая грамотность доц. Павлова Е.А. 305П";
        Wednesday_1[2] = "История доц. Лавлинский С.А. 292";
        Wednesday_1[4] = "СПЭД ст.преп. Копытина Е.А. 314П";
    }

    private void Init_Wednesday_0() {
        Wednesday_0[1] = "Экономика и финансовая грамотность доц. Павлова Е.А. 305П";
        Wednesday_0[2] = "История доц. Лавлинский С.А. 292";
        Wednesday_0[3] = "Деловое общение и культура речи доц. Саломатина М.С. 305П";
        Wednesday_0[4] = "Деловое общение и культура речи доц. Саломатина М.С. 477";
    }

    private void Init_Thursday_1() {
        Thursday_1[2] = "Математический анализ доц. Чуракова Т.А. 305П";
        Thursday_1[3] = "Ин.яз. преп. Гавриш О.А. 303П";
        Thursday_1[4] = "Физическая культура и спорт";
    }

    private void Init_Thursday_0() {
        Thursday_0[2] = "Математический анализ доц. Чуракова Т.А. 305П";
        Thursday_0[3] = "Ин.яз. преп. Гавриш О.А. 303П";
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

    private void Init_Saturday_1() {
        Saturday_1[1] = "История доц. Лавлинский С.А. 477";
    }

    private void Init_Saturday_0() {
        Saturday_0[1] = "История доц. Лавлинский С.А. 477";
        Saturday_0[2] = "Введение в программирование ст.преп. Соломатин Д.И. 385";
        Saturday_0[3] = "Введение в прогр. ст.преп. Соломатин Д.И. 383";
    }
}
