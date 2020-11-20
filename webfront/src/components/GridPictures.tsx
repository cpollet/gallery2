import React from "react";
import {Picture as ApiPicture} from "../API";
import Picture from "./Picture";

import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";

interface PropsType {
    pictures: ApiPicture[]
}

export default function GridPictures({pictures}: PropsType) {
    return (
        <Row>
            {pictures?.map(p => (
                <Col key={p.id} sm={12} md={6} lg={4} xl={4}>
                    <Picture picture={p}/>
                </Col>
            ))}
        </Row>
    );
}