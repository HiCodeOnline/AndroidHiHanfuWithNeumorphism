package online.hicode.android.hihanfu.data.repository

import kotlinx.coroutines.flow.Flow
import online.hicode.android.hihanfu.data.dao.HanfuDao
import online.hicode.android.hihanfu.data.entity.Hanfu
import online.hicode.android.hihanfu.data.entity.HanfuCabinet
import online.hicode.android.hihanfu.data.entity.HanfuImage
import online.hicode.android.hihanfu.data.entity.Jewelry
import online.hicode.android.hihanfu.data.entity.JewelryCabinet
import online.hicode.android.hihanfu.data.entity.JewelryImage
import online.hicode.android.hihanfu.data.entity.Outfit
import online.hicode.android.hihanfu.data.entity.OutfitHanfuRel
import online.hicode.android.hihanfu.data.entity.OutfitImage
import online.hicode.android.hihanfu.data.vo.StatisticsVO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HanfuRepository @Inject constructor(
    private val hanfuDao: HanfuDao
) {

    fun listHanfu(cabinetId: Long) : Flow<List<Hanfu>> {
        return hanfuDao.listHanfu(cabinetId)
    }

    fun listHanfuImageByHanfu(hanfuId: Long): Flow<List<HanfuImage>> {
        return hanfuDao.listHanfuImageByHanfu(hanfuId)
    }

    fun deleteHanfuImageByHanfu(cabinetId: Long) {
        hanfuDao.deleteHanfuImageByHanfu(cabinetId)
    }
    suspend fun deleteHanfuById(id: Long) {
        hanfuDao.deleteHanfuById(id)
    }
    suspend fun updateHanfu(hanfu: Hanfu) {
        hanfuDao.updateHanfu(hanfu)
    }
    fun listHanfuCabinet() : Flow<List<HanfuCabinet>> {
        return hanfuDao.listHanfuCabinet()
    }

    fun countHanfuCabinet(): Flow<List<StatisticsVO>> {
        return hanfuDao.countHanfuCabinet()
    }

    fun listHanfuImageByCabinet(cabinetId: Long): List<HanfuImage> {
        return hanfuDao.listHanfuImageByCabinet(cabinetId)
    }

    fun deleteHanfuImageByCabinet(cabinetId: Long) {
        hanfuDao.deleteHanfuImageByCabinet(cabinetId)
    }

    fun deleteHanfuByCabinet(cabinetId: Long) {
        hanfuDao.deleteHanfuByCabinet(cabinetId)
    }

    suspend fun deleteHanfuCabinetById(id: Long) {
        hanfuDao.deleteHanfuCabinetById(id)
    }

    suspend fun updateHanfuCabinet(hanfuCabinet: HanfuCabinet) {
        hanfuDao.updateHanfuCabinet(hanfuCabinet)
    }

    suspend fun insertHanfuCabinet(hanfuCabinet: HanfuCabinet) {
        hanfuDao.insertHanfuCabinet(hanfuCabinet)
    }

    suspend fun insertHanfu(hanfu: Hanfu): Long {
        return hanfuDao.insertHanfu(hanfu)
    }

    suspend fun insertHanfuImage(hanfuImage: HanfuImage) {
        hanfuDao.insertHanfuImage(hanfuImage)
    }

    suspend fun deleteHanfuImageById(hanfuImageId: Long) {
        hanfuDao.deleteHanfuImageById(hanfuImageId)
    }


    // **************************   Jewelry   *****************************

    fun listJewelry(cabinetId: Long) : Flow<List<Jewelry>> {
        return hanfuDao.listJewelry(cabinetId)
    }

    fun listJewelryImageByJewelry(jewelryId: Long): Flow<List<JewelryImage>> {
        return hanfuDao.listJewelryImageByJewelry(jewelryId)
    }

    fun deleteJewelryImageByJewelry(jewelryId: Long) {
        hanfuDao.deleteJewelryImageByJewelry(jewelryId)
    }
    suspend fun deleteJewelryById(id: Long) {
        hanfuDao.deleteJewelryById(id)
    }
    suspend fun updateJewelry(jewelry: Jewelry) {
        hanfuDao.updateJewelry(jewelry)
    }
    fun listJewelryCabinet() : Flow<List<JewelryCabinet>> {
        return hanfuDao.listJewelryCabinet()
    }

    fun countJewelryCabinet(): Flow<List<StatisticsVO>> {
        return hanfuDao.countJewelryCabinet()
    }

    fun listJewelryImageByCabinet(cabinetId: Long): List<JewelryImage> {
        return hanfuDao.listJewelryImageByCabinet(cabinetId)
    }

    fun deleteJewelryImageByCabinet(cabinetId: Long) {
        hanfuDao.deleteJewelryImageByCabinet(cabinetId)
    }

    fun deleteJewelryByCabinet(cabinetId: Long) {
        hanfuDao.deleteJewelryByCabinet(cabinetId)
    }

    suspend fun deleteJewelryCabinetById(id: Long) {
        hanfuDao.deleteJewelryCabinetById(id)
    }

    suspend fun updateJewelryCabinet(jewelryCabinet: JewelryCabinet) {
        hanfuDao.updateJewelryCabinet(jewelryCabinet)
    }

    suspend fun insertJewelryCabinet(jewelryCabinet: JewelryCabinet) {
        hanfuDao.insertJewelryCabinet(jewelryCabinet)
    }

    suspend fun insertJewelry(jewelry: Jewelry): Long {
        return hanfuDao.insertJewelry(jewelry)
    }

    suspend fun insertJewelryImage(jewelryImage: JewelryImage) {
        hanfuDao.insertJewelryImage(jewelryImage)
    }

    suspend fun deleteJewelryImageById(jewelryImageId: Long) {
        hanfuDao.deleteJewelryImageById(jewelryImageId)
    }



    // ************************  Outfit  **************************

    fun listOutfit() : Flow<List<Outfit>> {
        return hanfuDao.listOutfit()
    }

    fun listOutfitImageByOutfit(outfitId: Long): Flow<List<OutfitImage>> {
        return hanfuDao.listOutfitImageByOutfit(outfitId)
    }

    fun deleteOutfitImageByOutfit(outfitId: Long) {
        hanfuDao.deleteOutfitImageByOutfit(outfitId)
    }

    suspend fun deleteOutfitById(id: Long) {
        hanfuDao.deleteOutfitById(id)
    }

    suspend fun updateOutfit(outfit: Outfit) {
        hanfuDao.updateOutfit(outfit)
    }

    suspend fun insertOutfit(outfit: Outfit): Long {
        return hanfuDao.insertOutfit(outfit)
    }

    suspend fun insertOutfitImage(outfitImage: OutfitImage) {
        hanfuDao.insertOutfitImage(outfitImage)
    }

    suspend fun deleteOutfitImageById(id: Long) {
        hanfuDao.deleteOutfitImageById(id)
    }

    fun listHanfuByOutfit(outfitId: Long): Flow<List<Hanfu>> {
        return hanfuDao.listHanfuByOutfit(outfitId)
    }

    fun listJewelryByOutfit(outfitId: Long): Flow<List<Jewelry>> {
        return hanfuDao.listJewelryByOutfit(outfitId)
    }

    fun listAllHanfu() : Flow<List<Hanfu>> {
        return hanfuDao.listAllHanfu()
    }

    fun listAllJewelry() : Flow<List<Jewelry>> {
        return hanfuDao.listAllJewelry()
    }

    fun deleteOutfitByOutfit(outfitId: Long) {
        hanfuDao.deleteOutfitByOutfit(outfitId)
    }

    fun insertOutfitHanfuRel(outfitHanfuRel: OutfitHanfuRel) {
        hanfuDao.insertOutfitHanfuRel(outfitHanfuRel)
    }

    fun getHanfuCabinetById(id: Long): Flow<HanfuCabinet> {
        return hanfuDao.getHanfuCabinetById(id)
    }

    fun getJewelryCabinetById(id: Long): Flow<JewelryCabinet> {
        return hanfuDao.getJewelryCabinetById(id)
    }


}