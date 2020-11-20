import React from "react";
import Spinner from "react-bootstrap/Spinner";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";

export default function Loading() {
    return (
        <Row>
            <Col md="auto">
                <span>
                    <Spinner animation="border" variant="dark" size="sm"/> loading ...
                </span>
            </Col>
        </Row>
    );
}