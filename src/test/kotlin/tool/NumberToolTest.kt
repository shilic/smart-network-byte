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

    @Test fun `Int to32Bits Intel`() =
        assertContentEquals(byteArrayOf(
            1, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0
        ), 1.to32Bits(DataType.Intel), "1 的 Intel 32 bits 应仅 bit0=1")

    @Test fun `Int to32Bits Motorola`() =
        assertContentEquals(byteArrayOf(
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 1
        ), 1.to32Bits(DataType.Motorola), "1 的 Motorola 32 bits 应仅 bit31=1")
    @Test fun `Int to32Bits Intel 2`() =
        assertContentEquals(byteArrayOf(
            0, 1, 1, 1, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0
        ), 14.to32Bits(DataType.Intel), "14 的 Intel 32 bits 应仅低4位=1")
    @Test fun `Int to32Bits Motorola 2`() =
        assertContentEquals(byteArrayOf(
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 1, 1, 1, 0
        ), 14.to32Bits(DataType.Motorola), "14 的 Motorola 32 bits 应高4位=1")
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
    @Test fun `Long toBytes Intel`() =
        assertContentEquals(
            byteArrayOf(
                0x08.toByte(), 0x07.toByte(), 0x06.toByte(), 0x05.toByte(),
                0x04.toByte(), 0x03.toByte(), 0x02.toByte(), 0x01.toByte()),
            0x0102_0304_0506_0708L.toBytes(DataType.Intel, 8),
            "Intel 应低位在前，最低字节 0x08 在索引0")

    @Test fun `Long toBytes Motorola`() =
        assertContentEquals(
            byteArrayOf(
                0x01.toByte(), 0x02.toByte(), 0x03.toByte(), 0x04.toByte(),
                0x05.toByte(), 0x06.toByte(), 0x07.toByte(), 0x08.toByte()),
            0x0102_0304_0506_0708L.toBytes(DataType.Motorola, 8),
            "Motorola 应高位在前，最高字节 0x01 在索引0")
    @Test fun `Long toBytes Intel 2`() =
        assertContentEquals(
            byteArrayOf(
                0x08.toByte(), 0x07.toByte(), 0x06.toByte(), 0x05.toByte(), 0x04.toByte(), 0x03.toByte()),
            0x0000_0304_0506_0708L.toBytes(DataType.Intel),
            "0x0000_0304_0506_0708L toBytes Intel")

    @Test fun `Long toBytes Motorola 2`() =
        assertContentEquals(
            byteArrayOf(
                0x03.toByte(), 0x04.toByte(), 0x05.toByte(), 0x06.toByte(), 0x07.toByte(), 0x08.toByte()),
            0x0000_0304_0506_0708L.toBytes(DataType.Motorola),
            "0x0000_0304_0506_0708L toBytes Motorola")

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
    @Test fun `Byte to8Bits Intel`() =
        assertContentEquals(byteArrayOf(0, 1, 1, 1, 0, 0, 0, 0), 14.toByte().to8Bits(DataType.Intel), "Intel 应低位在前")

    @Test fun `Byte to8Bits Motorola`() =
        assertContentEquals(byteArrayOf(0, 0, 0, 0, 1, 1, 1, 0), 14.toByte().to8Bits(DataType.Motorola), "Motorola 应高位在前")

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

    @Test fun `ByteArray toBitsSafe valid`() =
        assertContentEquals(byteArrayOf(1, 1, 1, 1, 1, 1, 1, 1),
            byteArrayOf(0xFF.toByte()).toBitsSafe(),
            "0xFF toBitsSafe 应为 8 个 1")

    @Test fun `ByteArray toBitsSafe null`() =
        assertContentEquals(byteArrayOf(0), (null as ByteArray?).toBitsSafe(),
            "null 应返回默认长度 1 的全零数组")

    @Test fun `ByteArray toBitsSafe with length`() =
        assertContentEquals(byteArrayOf(1, 0, 0, 0),
            byteArrayOf(0x01.toByte()).toBitsSafe(bitLength = 4),
            "0x01 取前4位应仅 bit0=1")

    @Test fun `ByteArray toBitsSafe 0`() =
        assertContentEquals(byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
            byteArrayOf(0x00.toByte()).toBitsSafe(),
            "0x00 toBitsSafe 应为 8 个 0")

    // ==================== ByteArray.from4BytesTo32Bits ====================

    @Test fun `from4BytesTo32Bits Intel`() =
        assertContentEquals(byteArrayOf(
            1, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0
        ), byteArrayOf(0x01.toByte(), 0, 0, 0).from4BytesTo32Bits(DataType.Intel),
            "0x01 的 Intel 32 bits 应仅 bit0=1")

    // ==================== ByteArray.from8BytesTo64Bits ====================

    @Test fun `from8BytesTo64Bits Intel`() =
        assertContentEquals(byteArrayOf(
            1, 1, 1, 1, 1, 1, 1, 1,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0
        ), byteArrayOf(0xFF.toByte(), 0,0,0,0,0,0,0).from8BytesTo64Bits(),
            "0xFF 的 Intel 64 bits 前8位应为1")

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
        assertContentEquals(intArrayOf(0xFF, 0, 0, 0, 0, 0, 0, 0),
            bits.from64bitsTo8IntArray(DataType.Intel),
            "前8位为1，第一个 byte 应为 0xFF")
    }

    // ==================== ByteArray.from64bitsTo8Bytes ====================

    @Test fun from64bitsTo8Bytes() {
        val bits = ByteArray(64).also { for (i in 0 until 8) it[i] = 1 }
        assertContentEquals(byteArrayOf(0xFF.toByte(), 0, 0, 0, 0, 0, 0, 0),
            bits.from64bitsTo8ByteArray(DataType.Intel),
            "前8位为1，第一个 byte 应为 0xFF")
    }

    // ==================== ByteArray.bitsToBytes ====================

    @Test fun `bitsToBytes exact`() {
        val bits = ByteArray(8) { 1 }
        assertContentEquals(byteArrayOf(0xFF.toByte()), bits.bitsToBytes(DataType.Intel),
            "8 个 1 -> 0xFF")
    }

    @Test fun `bitsToBytes padded`() {
        val bits = ByteArray(5) { 1 }
        assertContentEquals(byteArrayOf(0x1F.toByte()), bits.bitsToBytes(DataType.Intel),
            "5 个 1 补零 -> 0x1F")
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
