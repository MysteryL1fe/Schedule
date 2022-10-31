package com.example.schedule;

public class Schedule_1132 extends Schedule{
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
        Monday_1[2] = "Введение в прикладную информатику доц. Абрамов И.В. 505П";
        Monday_1[4] = "История доц. Лавлинский С.А. 305П";
        Monday_1[5] = "Математический анализ доц. Канищева О.И. 477";
    }

    private void Init_Monday_0() {
        Monday_0[0] = "Ин.яз. преп. Семенов В.П. 303П";
        Monday_0[1] = "Русский язык доц. Козельская Н.А. 292";
        Monday_0[2] = "Введение в прикладную информатику доц. Абрамов И.В. 314П";
        Monday_0[3] = "Русский язык доц. Козельская Н.А. 477";
        Monday_0[4] = "История доц. Лавлинский С.А. 305П";
        Monday_0[5] = "Математический анализ доц. Канищева О.И. 477";
    }

    private void Init_Tuesday_1() {
        Tuesday_1[0] = "СПЭД и офисное программирование ст.преп. Копытина Е.А. 316П";
        Tuesday_1[2] = "Web-программирование доц. Сычев А.В. 384";
        Tuesday_1[3] = "Физическая культура и спорт";
        Tuesday_1[4] = "Web-программирование доц. Сычев А.В. 479";
    }

    private void Init_Tuesday_0() {
        Tuesday_0[0] = "СПЭД и офисное программирование ст.преп. Копытина Е.А. 316П";
        Tuesday_0[1] = "СПЭД и офисное программирование ст.преп. Копытина Е.А. 293";
        Tuesday_0[3] = "Физическая культура и спорт";
        Tuesday_0[4] = "Web-программирование доц. Сычев А.В. 479";
    }

    private void Init_Wednesday_1() {}

    private void Init_Wednesday_0() {}

    private void Init_Thursday_1() {
        Thursday_1[0] = "Дискретная математика проф. Лобода А.В. 477";
        Thursday_1[1] = "Дискретная математика проф. Лобода А.В. 477";
        Thursday_1[2] = "Ин.яз. преп. Семенов В.П. 301П";
        Thursday_1[3] = "Физическая культура и спорт";
        Thursday_1[4] = "Программирование ст.преп. Соломатин Д.И. 383";
    }

    private void Init_Thursday_0() {
        Thursday_0[0] = "Дискретная математика проф. Лобода А.В. 477";
        Thursday_0[1] = "Дискретная математика проф. Лобода А.В. 477";
        Thursday_0[2] = "Практикум по дисциплине \"Програмиирование\" ст.преп. Соломатин Д.И. 385";
        Thursday_0[3] = "Физическая культура и спорт";
    }

    private void Init_Friday_1() {
        Friday_1[0] = "Программирование ст.преп. Соломатин Д.И. (ДО)";
        Friday_1[4] = "История доц. Лавлинский С.А. 477";
        Friday_1[5] = "Математический анализ асс. Решетова О.О. 292";
    }

    private void Init_Friday_0() {
        Friday_0[0] = "Программирование ст.преп. Соломатин Д.И. (ДО)";
        Friday_0[2] = "Системы подготовки электронных документов ст.преп. Копытина Е.А. (ДО)";
        Friday_0[4] = "История доц. Лавлинский С.А. 477";
        Friday_0[5] = "Математический анализ асс. Решетова О.О. 292";
    }

    private void Init_Saturday_1() {}

    private void Init_Saturday_0() {}
}
