package tool

import numberUtils.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertContentEquals

class NumberToolTest {

    // ==================== Int.toBits ====================

    @Test fun `Int toBits variable length Intel`() =
        assertContentEquals(byteArrayOf(0, 1, 1, 1), 14.toBits(DataType.Intel),
            "14 的 Intel 不定长 bits 应为 {0, 1, 1, 1}")

    @Test fun `Int toBits variable length Motorola`() =
        assertContentEquals(byteArrayOf(1, 1, 1, 0), 14.toBits(DataType.Motorola),
            "14 的 Motorola 不定长 bits 应为 {1, 1, 1, 0}")
    @Test fun `Int toBits variable length Intel 2`() =
        assertContentEquals(byteArrayOf(0, 1, 1, 1, 0, 0, 0, 0), 14.toBits(DataType.Intel, 8),
            "14 的 Intel 不定长 bits 应为 {0, 1, 1, 1, 0, 0, 0, 0}")

    @Test fun `Int toBits variable length Motorola 2`() =
        assertContentEquals(byteArrayOf(0, 0, 0, 0, 1, 1, 1, 0), 14.toBits(DataType.Motorola, 8),
            "14 的 Motorola 不定长 bits 应为 {0, 0, 0, 0, 1, 1, 1, 0}")

    @Test fun `Int toBits zero`() =
        assertContentEquals(byteArrayOf(0), 0.toBits(),
            "0 的 bits 应为0数组")

    @Test fun `Int toBits fixed length Intel`() =
        assertContentEquals(byteArrayOf(0, 1, 0, 0, 0), 2.toBits(DataType.Intel, 5),
            "2 的 Intel 定长5 bits 应为 {0, 1, 0, 0, 0}")

    @Test fun `Int toBits fixed length Motorola`() =
        assertContentEquals(byteArrayOf(0, 0, 0, 1, 0), 2.toBits(DataType.Motorola, 5),
            "2 的 Motorola 定长5 bits 应为 {0, 0, 0, 1, 0}（高位在左）")

    // ==================== Int.to32Bits ====================

    @Test fun `Int to32Bits Intel`() {
        val bits = 1.to32Bits(DataType.Intel)
        assertEquals(32, bits.size, "应返回 32 个元素")
        assertEquals(1, bits[0], "最低位应为 1")
        assertEquals(0, bits[31], "最高位应为 0")
    }

    @Test fun `Int to32Bits Motorola`() {
        val bits = 1.to32Bits(DataType.Motorola)
        assertEquals(32, bits.size, "应返回 32 个元素")
        assertEquals(1, bits[31], "Motorola 最低位在最高索引")
    }
    @Test fun `Int to32Bits Intel 2`() {
        val bits = 14.to32Bits(DataType.Intel)
        assertContentEquals(byteArrayOf(
            0, 1, 1, 1, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0
        ), bits,
            "14 的 Intel 定长 32 bits 应为:\n { 0, 1, 1, 1, 0, 0, 0, 0,\n" +
                    "   0, 0, 0, 0, 0, 0, 0, 0,\n" +
                    "   0, 0, 0, 0, 0, 0, 0, 0,\n" +
                    "   0, 0, 0, 0, 0, 0, 0, 0}\n")

    }
    @Test fun `Int to32Bits Motorola 2`() {
        val bits = 14.to32Bits(DataType.Motorola)
        assertContentEquals(byteArrayOf(
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 1, 1, 1, 0
        ), bits,
            "14 的 Intel 定长 32 bits 应为:\n { 0, 0, 0, 0, 0, 0, 0, 0,\n" +
                    "   0, 0, 0, 0, 0, 0, 0, 0,\n" +
                    "   0, 0, 0, 0, 0, 0, 0, 0,\n" +
                    "   0, 0, 0, 0, 1, 1, 1, 0}\n")

    }
    // ==================== Int.toBytesI ====================

    @Test fun `Int toBytesI Intel`() =
        assertContentEquals(intArrayOf(0x1B, 0x01, 0xFE, 0x18), 0x18FE011B.toBytesI(DataType.Intel),
            "0x18FE011B Intel 应低位在前")

    @Test fun `Int toBytesI Motorola`() =
        assertContentEquals(intArrayOf(0x18, 0xFE, 0x01, 0x1B), 0x18FE011B.toBytesI(DataType.Motorola),
            "0x18FE011B Motorola 应高位在前")

    // ==================== Int.to4Bytes ====================

    @Test fun `Int to4Bytes Intel`() =
        assertContentEquals(byteArrayOf(0x1B.toByte(), 0x01.toByte(), 0xFE.toByte(), 0x18.toByte()),
            0x18FE011B.to4Bytes(DataType.Intel), "0x18FE011B Intel to4Bytes")

    @Test fun `Int to4Bytes Motorola`() =
        assertContentEquals(byteArrayOf(0x18.toByte(), 0xFE.toByte(), 0x01.toByte(), 0x1B.toByte()),
            0x18FE011B.to4Bytes(DataType.Motorola), "0x18FE011B Motorola to4Bytes")
    @Test fun `Int to4Bytes Intel 2`() =
        assertContentEquals(byteArrayOf(0x1B.toByte(), 0x01.toByte(), 0xFE.toByte(), 0x00.toByte()),
            0x00FE011B.to4Bytes(DataType.Intel), "0x00FE011B Intel to4Bytes")

    @Test fun `Int to4Bytes Motorola 2`() =
        assertContentEquals(byteArrayOf(0x00.toByte(), 0xFE.toByte(), 0x01.toByte(), 0x1B.toByte()),
            0x00FE011B.to4Bytes(DataType.Motorola), "0x00FE011B Motorola to4Bytes")

    // ==================== Int.toBytes ====================

    @Test fun `Int toBytes Intel length 3`() =
        assertContentEquals(byteArrayOf(3, 2, 1), 0x010203.toBytes(DataType.Intel, 3),
            "Intel 应低位在前")

    @Test fun `Int toBytes Motorola length 3`() =
        assertContentEquals(byteArrayOf(1, 2, 3), 0x010203.toBytes(DataType.Motorola, 3),
            "Motorola 应高位在前")

    @Test fun `Int toBytes Intel`() =
        assertContentEquals(byteArrayOf(3, 2, 1), 0x010203.toBytes(DataType.Intel),
            "Intel 应低位在前")
    @Test fun `Int toBytes Motorola`() =
        assertContentEquals(byteArrayOf(1, 2, 3), 0x010203.toBytes(DataType.Motorola),
            "Motorola 应高位在前")
    @Test fun `Int toBytes Intel length 4`() =
        assertContentEquals(byteArrayOf(3, 2, 1, 0), 0x0001_0203.toBytes(DataType.Intel, 4),
            "Intel 应低位在前")

    @Test fun `Int toBytes Motorola length 4`() =
        assertContentEquals(byteArrayOf(0, 1, 2, 3), 0x0001_0203.toBytes(DataType.Motorola, 4),
            "Motorola 应高位在前")

    // ==================== Long.toBytes ====================
    @Test fun `Long toBytes Intel`() {
        val result = 0x0102_0304_0506_0708L.toBytes(DataType.Intel, 8)
        assertEquals(8, result.size, "应返回 8 个元素")
        assertEquals(0x08.toByte(), result[0], "Intel 最低字节在前")
    }

    @Test fun `Long toBytes Motorola`() {
        val result = 0x0102_0304_0506_0708L.toBytes(DataType.Motorola, 8)
        assertEquals(0x01.toByte(), result[0], "Motorola 最高字节在前")
    }
    @Test fun `Long toBytes Intel 2`() {
        val result = 0x0000_0304_0506_0708L.toBytes(DataType.Intel)
        assertContentEquals(
            byteArrayOf(0x08.toByte(), 0x07.toByte(), 0x06.toByte(), 0x05.toByte(), 0x04.toByte(), 0x03.toByte()),
            result,
            "0x0000_0304_0506_0708L toBytes Intel")
        assertEquals(6, result.size, "应返回 6 个元素")
    }
    @Test fun `Long toBytes Motorola 2`() {
        val result = 0x0000_0304_0506_0708L.toBytes(DataType.Motorola)
        assertContentEquals(
            byteArrayOf(0x03.toByte(), 0x04.toByte(), 0x05.toByte(), 0x06.toByte(), 0x07.toByte(), 0x08.toByte()),
            result,
            "0x0000_0304_0506_0708L toBytes Motorola")
        assertEquals(6, result.size, "应返回 6 个元素")
    }

    // ==================== Int.effectiveByteCount =====================
    @Test fun `Int effectiveByteCount`() =
        assertEquals(4, 0x12345678.effectiveByteCount(), "0x12345678 占 4 字节")
    @Test fun `Int effectiveByteCount 2`() =
        assertEquals(3, 0x00045678.effectiveByteCount(), "0x00045678 占 3 字节")

    // ==================== Long.effectiveByteCount ====================
    @Test fun `Long effectiveByteCount`() =
        assertEquals(4, 0x12345678L.effectiveByteCount(), "0x12345678 占 4 字节")
    @Test fun `Long effectiveByteCount 2`() =
        assertEquals(3, 0x00345678L.effectiveByteCount(), "0x00345678 占 3 字节")
    @Test fun `Long effectiveByteCount zero`() =
        assertEquals(0, 0L.effectiveByteCount(), "0 的有效字节数为 0")

    // ==================== Byte.to8Bits ====================
    @Test fun `Byte to8Bits Intel`() {
        val bits = 14.toByte().to8Bits(DataType.Intel)
        assertEquals(8, bits.size)
        assertContentEquals(byteArrayOf(0, 1, 1, 1, 0, 0, 0, 0), bits, "Intel 应低位在前")
    }

    @Test fun `Byte to8Bits Motorola`() {
        val bits = 14.toByte().to8Bits(DataType.Motorola)
        assertContentEquals(byteArrayOf(0, 0, 0, 0, 1, 1, 1, 0), bits, "Motorola 应高位在前")
    }

    // ==================== Byte.toBits ====================

    @Test fun `Byte toBits Intel length 4`() =
        assertContentEquals(byteArrayOf(0, 1, 0, 0, 0 ,0 ), 2.toByte().toBits(DataType.Intel, 6),
            "Intel 应为 {0, 1, 0, 0, 0 ,0 }")

    @Test fun `Byte toBits Motorola length 4`() =
        assertContentEquals(byteArrayOf(0, 0, 0, 0, 1, 0), 2.toByte().toBits(DataType.Motorola, 6),
            "Motorola 应为 {0, 0, 0, 0, 1, 0}（高位在左）")
    @Test fun `Byte toBits Intel `() =
        assertContentEquals(byteArrayOf(0, 1, 1, 1), 14.toByte().toBits(DataType.Intel),
            "应为 {0, 1, 1, 1}")

    @Test fun `Byte toBits Motorola`() =
        assertContentEquals(byteArrayOf(1, 1, 1, 0), 14.toByte().toBits(DataType.Motorola),
            "应为 {1, 1, 1, 0}（高位在左）")

    // ==================== ByteArray?.toBitsSafe ====================

    @Test fun `ByteArray toBitsSafe valid`() {
        val result = byteArrayOf(0xFF.toByte()).toBitsSafe()
        assertEquals(8, result.size)
        assertEquals(1, result[0])
    }

    @Test fun `ByteArray toBitsSafe null`() =
        assertEquals(1, (null as ByteArray?).toBitsSafe().size,
            "null 应返回默认长度 1 的全零数组")

    @Test fun `ByteArray toBitsSafe with length`() {
        val result = byteArrayOf(0x01.toByte()).toBitsSafe(bitLength = 4)
        assertEquals(4, result.size, "指定 bitLength=4 应只返回 4 个元素")
    }
    @Test fun `ByteArray toBitsSafe 0`() {
        val result = byteArrayOf(0x00.toByte()).toBitsSafe()
        assertEquals(8, result.size, "默认长度 8 的全零数组")
    }

    // ==================== ByteArray.from4BytesTo32Bits ====================

    @Test fun `from4BytesTo32Bits Intel`() {
        val bits = byteArrayOf(0x01.toByte(), 0, 0, 0).from4BytesTo32Bits(DataType.Intel)
        assertEquals(32, bits.size)
        assertEquals(1, bits[0], "0x01 的 bit0 应为 1")
    }

    // ==================== ByteArray.from8BytesTo64Bits ====================

    @Test fun `from8BytesTo64Bits Intel`() {
        val bits = byteArrayOf(0xFF.toByte(), 0,0,0,0,0,0,0).from8BytesTo64Bits()
        assertEquals(64, bits.size)
        assertEquals(1, bits[0], "0xFF 第一个 bit 应为 1")
    }

    // ==================== ByteArray.bitsToInt ====================

    @Test fun `bitsToInt Intel`() =
        assertEquals(15, byteArrayOf(1,1,1,1).bitsToInt(DataType.Intel),
            "Intel {1,1,1,1} -> 15")

    @Test fun `bitsToInt Motorola`() =
        assertEquals(15, byteArrayOf(1,1,1,1).bitsToInt(DataType.Motorola),
            "Motorola {1,1,1,1} -> 15")

    // ==================== ByteArray.from8bitsToInt ====================

    @Test fun `from8bitsToInt Intel`() =
        assertEquals(0xFF, byteArrayOf(1,1,1,1,1,1,1,1).from8bitsToInt(DataType.Intel),
            "全1 8bits Intel -> 0xFF")

    // ==================== ByteArray.from8bitsToByte ====================

    @Test fun `from8bitsToByte Intel`() =
        assertEquals(0xAB.toByte(), byteArrayOf(1,1,0,1,0,1,0,1).from8bitsToByte(DataType.Intel),
            "8bits Intel -> 0xAB")

    // ==================== ByteArray.toBytePadded ====================

    @Test fun `toBytePadded short bits`() =
        assertEquals(3.toByte(), byteArrayOf(1,1).toBytePadded(DataType.Intel),
            "{1,1} 补零后应等于 3")

    // ==================== ByteArray.from32bitsToInt ====================

    @Test fun from32bitsToInt() {
        val bits = ByteArray(32).also { it[0] = 1; it[1] = 1; it[2] = 1; it[3] = 1 }
        assertEquals(15, bits.from32bitsToInt(DataType.Intel), "前4位为1，应等于 15")
    }

    // ==================== ByteArray.from64bitsTo8IntArray ====================

    @Test fun from64bitsTo8IntArrayTest() {
        val bits = ByteArray(64).also { for (i in 0 until 8) it[i] = 1 }
        val result = bits.from64bitsTo8IntArray(DataType.Intel)
        assertEquals(8, result.size, "应返回 8 个 Int")
        assertEquals(0xFF, result[0], "第一个 byte 应为 0xFF")
    }

    // ==================== ByteArray.from64bitsTo8Bytes ====================

    @Test fun from64bitsTo8Bytes() {
        val bits = ByteArray(64).also { for (i in 0 until 8) it[i] = 1 }
        val result = bits.from64bitsTo8ByteArray(DataType.Intel)
        assertEquals(8, result.size, "应返回 8 个 Byte")
        assertEquals(0xFF.toByte(), result[0], "第一个 byte 应为 0xFF")
    }

    // ==================== ByteArray.bitsToBytes ====================

    @Test fun `bitsToBytes exact`() {
        val bits = ByteArray(8) { 1 }
        val result = bits.bitsToBytes(DataType.Intel)
        assertEquals(1, result.size, "8 bits = 1 byte")
        assertEquals(0xFF.toByte(), result[0], "全1 -> 0xFF")
    }

    @Test fun `bitsToBytes padded`() {
        val bits = ByteArray(5) { 1 }
        val result = bits.bitsToBytes(DataType.Intel)
        assertEquals(1, result.size, "5 bits 补零后 = 1 byte")
    }

    // ==================== ByteArray.from4BytesToInt ====================

    @Test fun `from4BytesToInt Intel`() =
        assertEquals(0x18FE011B, byteArrayOf(0x1B.toByte(), 0x01.toByte(), 0xFE.toByte(), 0x18.toByte())
            .from4BytesToInt(DataType.Intel), "Intel 4bytes -> 0x18FE011B")

    @Test fun `from4BytesToInt Motorola`() =
        assertEquals(0x18FE011B, byteArrayOf(0x18.toByte(), 0xFE.toByte(), 0x01.toByte(), 0x1B.toByte())
            .from4BytesToInt(DataType.Motorola), "Motorola 4bytes -> 0x18FE011B")

    // ==================== ByteArray.bytesToInt ====================

    @Test fun `bytesToInt Intel`() =
        assertEquals(0x010203, byteArrayOf(3, 2, 1).bytesToInt(DataType.Intel),
            "Intel {0x03,0x02,0x01} -> 0x010203")

    @Test fun `bytesToInt Motorola`() =
        assertEquals(0x010203, byteArrayOf(1, 2, 3).bytesToInt(DataType.Motorola),
            "Motorola {0x01,0x02,0x03} -> 0x010203")

    // ==================== ByteArray.bytesToLong ====================

    @Test fun `bytesToLong Intel`() =
        assertEquals(0x0102030405060708L,
            byteArrayOf(0x08.toByte(),0x07.toByte(),0x06.toByte(),0x05.toByte(),
                0x04.toByte(),0x03.toByte(),0x02.toByte(),0x01.toByte()).bytesToLong(DataType.Intel),
            "Intel 8bytes -> Long")

    @Test fun `bytesToLong Motorola`() =
        assertEquals(0x0102030405060708L,
            byteArrayOf(0x01.toByte(),0x02.toByte(),0x03.toByte(),0x04.toByte(),
                0x05.toByte(),0x06.toByte(),0x07.toByte(),0x08.toByte()).bytesToLong(DataType.Motorola),
            "Motorola 8bytes -> Long")

    // ==================== ByteArray.from4BytesToLong ====================

    @Test fun `from4BytesToLong Intel`() =
        assertEquals(0x18FE011BL,
            byteArrayOf(0x1B.toByte(), 0x01.toByte(), 0xFE.toByte(), 0x18.toByte())
                .from4BytesToLong(DataType.Intel), "Intel 4bytes -> Long")

    @Test fun `from4BytesToLong Motorola`() =
        assertEquals(0x18FE011BL,
            byteArrayOf(0x18.toByte(), 0xFE.toByte(), 0x01.toByte(), 0x1B.toByte())
                .from4BytesToLong(DataType.Motorola), "Motorola 4bytes -> Long")

    // ==================== Byte.toIntWithLower ====================

    @Test fun `Byte toIntWithLower`() =
        assertEquals(0xAFFE, 0xAF.toByte().toIntWithLower(0xFE.toByte()),
            "0xAF + 0xFE -> 0xAFFE")

}
