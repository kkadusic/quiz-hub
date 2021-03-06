import React, {useEffect, useState} from 'react';
import { Col, message, Row } from "antd";
import MyCard from "components/MyCard";
import { getAllCategories } from "api/quiz/category";
import { categoriesUrl, quizUrl } from "utilities/appUrls";
import { useHistory } from "react-router-dom";
import { getQuizzesByCategory } from "api/quiz/quiz";

const Categories = (props) => {
    const [data, setData] = useState([]);
    const history = useHistory();
    const id = props.match?.params[0];

    useEffect(() => {
        const fetchData = async () => {
            try {
                if (id) {
                    setData(await getQuizzesByCategory(id));
                } else {
                    setData(await getAllCategories());
                }
            } catch (error) {
                message.warning(error.response.data.message);
            }
        }

        fetchData();
    }, [id])

    return (
        <div>
            <Row gutter={[32, 32]}>
                <Col span={24}>
                    <h2 style={{ textAlign: 'left', marginBottom: 10 }}>
                        { id ? "QUIZZES" : "ALL CATEGORIES" }
                    </h2>
                </Col>
            </Row>
            <Row gutter={[32, 32]}>
                {data.map(entity =>
                    <Col xs={12} sm={8} md={4}>
                        <MyCard onClick={() => history.push((id ? quizUrl : categoriesUrl) + "/" + entity.id)} key={entity.id} imgSrc={id ? entity?.category?.imageUrl : entity.imageUrl} title={entity.name} />
                    </Col>
                )}
                {data.length === 0 &&
                    <Col style={{ textAlign: 'left' }} span={24}>
                        <h2>
                            { id ? "No quizzes" : "No categories"}
                        </h2>
                    </Col>
                }
            </Row>
        </div>
    );
}

export default Categories;
