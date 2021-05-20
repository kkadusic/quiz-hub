import { get } from "./common";

const quizzesUrl = "/quizzes";

export const getAllQuizzes = async (tournamentId) => {
    return await get(quizzesUrl + "/all?tournament=" + tournamentId);
}

export const getQuizzesByCategory = async (categoryId) => {
    return await get(quizzesUrl + "/category?id=" + categoryId);
}
