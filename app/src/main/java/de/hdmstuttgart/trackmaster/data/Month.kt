package de.hdmstuttgart.trackmaster.data

enum class Month (val number: String, val length: Int) {
    JAN("01", 31),
    FEB("02", 28),
    MAR("03", 31),
    APR("04", 30),
    MAY("05", 31),
    JUN("06", 30),
    JUL("07", 31),
    AUG("08", 31),
    SEP("09", 30),
    OCT("10", 31),
    NOV("11", 30),
    DEC("12", 31)
}