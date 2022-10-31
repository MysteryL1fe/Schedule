package com.example.schedule;

public class Schedule_1032 extends Schedule{
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
        Monday_1[3] = "Экономика и финансовая грамотность доц. Павлова Е.А. 477";
    }

    private void Init_Monday_0() {
        Monday_0[3] = "Экономика и финансовая грамотность доц. Павлова Е.А. 291";
    }

    private void Init_Tuesday_1() {
        Tuesday_1[1] = "Математический анализ доц. Сирота Е.А. 505П";
        Tuesday_1[3] = "СПЭД асс. Сафонов В.Г. 303П";
        Tuesday_1[4] = "Физическая культура и спорт";
        Tuesday_1[5] = "История доц. Лавлинский С.А. 477";
    }

    private void Init_Tuesday_0() {
        Tuesday_0[1] = "Математический анализ доц. Сирота Е.А. 505П";
        Tuesday_0[3] = "Ин.яз. доц. Барабушка И.А. 309П";
        Tuesday_0[4] = "Физическая культура и спорт";
        Tuesday_0[5] = "История доц. Лавлинский С.А. 477";
    }

    private void Init_Wednesday_1() {
        Wednesday_1[3] = "История доц. Лавлинский С.А. 292";
        Wednesday_1[4] = "Математический анализ доц. Чуракова Т.А. 505П";
        Wednesday_1[5] = "Деловое общение и культура речи доц. Саломатина М.С. 307П";
    }

    private void Init_Wednesday_0() {
        Wednesday_0[3] = "История доц. Лавлинский С.А. 292";
        Wednesday_0[4] = "Математический анализ доц. Чуракова Т.А. 505П";
        Wednesday_0[5] = "Деловое общение и культура речи доц. Саломатина М.С. 479";
    }

    private void Init_Thursday_1() {
        Thursday_1[2] = "Ин.яз. доц. Барабушка И.А. 309П";
        Thursday_1[3] = "Введение в программирование ст.преп. Соломатин Д.И. 384";
        Thursday_1[4] = "Физическая культура и спорт";
        Thursday_1[5] = "Теоретические основы информатики доц. Самодуров А.С. 384";
    }

    private void Init_Thursday_0() {
        Thursday_0[2] = "Ин.яз. доц. Барабушка И.А. 309П";
        Thursday_0[3] = "Введение в прогр. ст.преп. Соломатин Д.И. 384";
        Thursday_0[4] = "Физическая культура и спорт";
        Thursday_0[5] = "Теоретические основы инф. доц. Самодуров А.С. 295";
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
