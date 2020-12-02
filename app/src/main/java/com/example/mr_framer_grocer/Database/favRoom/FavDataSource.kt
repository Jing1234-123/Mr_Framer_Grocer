package com.example.mr_framer_grocer.Database.favRoom

class FavDataSource(private var favDao: FavDao):  IFavDataSource{

    companion object{
        fun getInstance(favDao: FavDao): FavDataSource {
            val instance = FavDataSource(favDao)
            return instance
        }
    }

    override fun getFavItems(): List<Fav> {
        return favDao.getFavItems()
    }

    override fun isFav(itemId: Int): List<Fav> {
        return favDao.isFav(itemId)
    }

    override fun deleteFavItem(fav: Fav) {
        return favDao.deleteFavItem(fav)
    }

    override fun addToFav(vararg fav: Fav) {
        favDao.addToFav(*fav)
    }

    override fun delById(itemId: Int) {
        favDao.delById(itemId)
    }

    override fun emptyFav() {
        favDao.emptyFav()
    }


}