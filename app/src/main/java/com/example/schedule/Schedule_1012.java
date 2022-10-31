package com.example.schedule;

public class Schedule_1012 extends Schedule{

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

    public void Init_Monday_1() {
        Monday_1[1] = "История доц. Лавлинский С.А. 305П";
        Monday_1[3] = "Ин.яз. доц. Стрельникова М.А. 309П";
    }

    public void Init_Monday_0() {
        Monday_0[1] = "История доц. Лавлинский С.А. 305П";
        Monday_0[2] = "Ин.яз. доц. Стрельникова М.А. 308П";
    }

    public void Init_Tuesday_1() {
        Tuesday_1[1] = "Математический анализ доц. Сирота Е.А. 505П";
        Tuesday_1[2] = "СПЭД асс. Сафонов В.Г. 314П";
        Tuesday_1[4] = "Физическая культура и спорт";
        Tuesday_1[5] = "История доц. Лавлинский С.А. 477";
    }

    public void Init_Tuesday_0() {
        Tuesday_0[1] = "Математический анализ доц. Сирота Е.А. 505П";
        Tuesday_0[4] = "Физическая культура и спорт";
        Tuesday_0[5] = "История доц. Лавлинский С.А. 477";
    }

    public void Init_Wednesday_1() {
        Wednesday_1[1] = "Введение в программирование ст.преп. Соломатин Д.И. 291";
        Wednesday_1[3] = "Математический анализ доц. Чуракова Т.А. 477";
        Wednesday_1[4] = "Экономика и финансовая грамотность доц. Павлова Е.А. 380";
    }

    public void Init_Wednesday_0() {
        Wednesday_0[1] = "Введение в прогр. ст.преп. Соломатин Д.И. 385";
        Wednesday_0[2] = "Ин.яз. доц. Стрельникова М.А. 308П";
        Wednesday_0[3] = "Математический анализ доц. Чуракова Т.А. 477";
        Wednesday_0[4] = "Экономика и финансовая грамотность доц. Павлова Е.А. 380";
        Wednesday_0[5] = "Деловое общение и культура речи доц. Саломатина М.С. 479";
    }

    public void Init_Thursday_1() {
        Thursday_1[4] = "Физическая культура и спорт";
    }

    public void Init_Thursday_0() {
        Thursday_0[3] = "Деловое общение и культура речи доц. Саломатина М.С. 477";
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

    public void Init_Saturday_1() {
        Saturday_1[4] = "Теоретические основы информатики доц. Самодуров А.С. 291";
        Saturday_1[5] = "Теоретические основы инф. доц. Самодуров А.С. 387";
    }

    public void Init_Saturday_0() {}
}
