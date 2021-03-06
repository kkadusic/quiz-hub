import { del, get, post } from "./common";

const favoritesUrl = "/favorites";

export const getAllFavoritesByQuiz = async (id) => {
    return await get(favoritesUrl + "/all/quiz?id=" + id);
}

export const getAllFavoritesByUser = async (username) => {
    return await get(favoritesUrl + "/all/user?username=" + username);
}

export const addFavorite = async (body) => {
    return await post(favoritesUrl, body);
}

export const deleteFavorite = async (id) => {
    return await del(favoritesUrl + "?id=" + id);
}
