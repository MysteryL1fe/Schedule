package com.example.schedule;

public class Schedule_1093 extends Schedule{
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
        Monday_1[1] = "Деловое общение и культура речи доц. Козельская Н.А. 380";
        Monday_1[2] = "Экономика и финансовая грамотность доц. Павлова Е.А. 292";
    }

    private void Init_Monday_0() {
        Monday_0[2] = "Экономика и финансовая грамотность доц. Павлова Е.А. 292";
    }

    private void Init_Tuesday_1() {
        Tuesday_1[2] = "Ин.яз. преп. Гафарова М.Х. 505П";
        Tuesday_1[3] = "Ин.яз. преп. Гафарова М.Х. 301П";
        Tuesday_1[4] = "История доц. Лавлинский С.А. 477";
        Tuesday_1[5] = "Физическая культура и спорт";
    }

    private void Init_Tuesday_0() {
        Tuesday_0[3] = "Ин.яз. преп. Гафарова М.Х. 387";
        Tuesday_0[4] = "История доц. Лавлинский С.А. 477";
        Tuesday_0[5] = "Физическая культура и спорт";
    }

    private void Init_Wednesday_1() {
        Wednesday_1[0] = "Математический анализ доц. Минин Л.А. 477";
        Wednesday_1[1] = "История доц. Лавлинский С.А. 292";
    }

    private void Init_Wednesday_0() {
        Wednesday_0[0] = "Математический анализ доц. Минин Л.А. 477";
        Wednesday_0[1] = "История доц. Лавлинский С.А. 292";
        Wednesday_0[2] = "СПЭД асс. Сафонов В.Г. 384";
    }

    private void Init_Thursday_1() {
        Thursday_1[2] = "Математический анализ ст.пре. Пашкевич А.А. 477";
        Thursday_1[3] = "Физическая культура и спорт";
    }

    private void Init_Thursday_0() {
        Thursday_0[2] = "Математический анализ ст.пре. Пашкевич А.А. 477";
        Thursday_0[3] = "Физическая культура и спорт";
    }

    private void Init_Friday_1() {
        Friday_1[0] = "Введение в программирование ст.преп. Соломатин Д.И. (ДО)";
        Friday_1[1] = "Информатика доц. Сычев А.В. (ДО)";
        Friday_1[2] = "Экономика и финансовая грамотность доц. Павлова Е.А. (ДО)";
        Friday_1[3] = "Деловое общение и культура речи доц. Козельская Н.А. (ДО)";
        Friday_1[4] = "Введение в программную инженерию проф. Чижов М.И. (ДО)";
    }

    private void Init_Friday_0() {
        Friday_0[0] = "Введение в программирование ст.преп. Соломатин Д.И. (ДО)";
        Friday_0[1] = "Информатика доц. Сычев А.В. (ДО)";
        Friday_0[2] = "Системы подготовки электронных документов ст.преп. Копытина Е.А. (ДО)";
        Friday_0[4] = "Введение в программную инженерию проф. Чижов М.И. (ДО)";
    }

    private void Init_Saturday_1() {
        Saturday_1[3] = "Введение в программирование ст.преп. Соломатин Д.И. 385";
        Saturday_1[4] = "Информатика асс. Трухачев С.А. 383";
    }

    private void Init_Saturday_0() {
        Saturday_0[3] = "Введение в программирование ст.преп. Соломатин Д.И. 385";
        Saturday_0[4] = "Информатика асс. Трухачев С.А. 383";
    }
}
