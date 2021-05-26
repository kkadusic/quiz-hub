import React, { useEffect, useState } from 'react';
import { getAllQuizzes } from "api/quiz/quiz";
import { getAllTournaments } from "api/tournament/tournament";
import { Col, Row } from "antd";
import MyCard from "components/MyCard";
import { quizUrl, tournamentUrl } from "utilities/appUrls";
import { useHistory } from "react-router-dom";
import QuizCarousel from "components/QuizCarousel";
import { getRandom } from "utilities/common";
import Leaderboard from "components/Leaderboard";
import { getAllScoresToday } from "api/property/score";

const LandingPage = () => {
    const history = useHistory();
    const [quizzes, setQuizzes] = useState([]);
    const [scores, setScores] = useState([]);
    const [tournaments, setTournaments] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);
                setQuizzes(await getAllQuizzes(false));
                setScores(await getAllScoresToday());
                setTournaments(await getAllTournaments(false));
                setLoading(false);
            } catch (error) {
                setLoading(false);
                //message.warning(error.response.data.message);
            }
        }

        fetchData();
    }, [])

    return (
        <div className="landing-page-container">
            <Row gutter={[32, 32]}>
                <Col span={12}>
                    <h2 style={{ textAlign: 'left' }}>
                        RANDOM QUIZZES
                    </h2>
                </Col>
                <Col span={12}>
                    <h2 style={{ textAlign: 'left' }}>
                        LATEST PLAYS
                    </h2>
                </Col>
            </Row>
            <Row gutter={[32, 32]}>
                <Col span={12}>
                    <QuizCarousel quizzes={getRandom(quizzes, 5)} />
                </Col>
                <Col span={12}>
                    <Leaderboard loading={loading} noRank scores={scores} />
                </Col>
            </Row>
            <Row gutter={[32, 32]}>
                <Col span={24}>
                    <h2 style={{ textAlign: 'left', marginTop: 20, marginBottom: 10 }}>
                        ALL QUIZZES
                    </h2>
                </Col>
            </Row>
            <Row gutter={[32, 32]}>
                {quizzes.map(quiz =>
                    <Col xs={12} sm={8} md={4}>
                        <MyCard onClick={() => history.push( quizUrl + "/" + quiz.id)} key={quiz.id} imgSrc={quiz?.category?.imageUrl} title={quiz.name} />
                    </Col>
                )}
            </Row>
            <Row gutter={[32, 32]}>
                <Col span={24}>
                    <h2 style={{ textAlign: 'left', marginTop: 20, marginBottom: 10 }}>
                        TOURNAMENTS
                    </h2>
                </Col>
            </Row>
            <Row gutter={[32, 32]}>
                {tournaments.map(tournament =>
                    <Col xs={12} sm={8} md={4}>
                        <MyCard onClick={() => history.push(tournamentUrl + "/" + tournament.id)} key={tournament.id} title={tournament.name} />
                    </Col>
                )}
            </Row>
        </div>
    );
}

export default LandingPage;
