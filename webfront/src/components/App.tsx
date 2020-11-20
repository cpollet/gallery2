import React from 'react';
import Container from "react-bootstrap/Container";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Gallery from "./Gallery";

function App() {
    return (
        <div className="App">
            <Container>
                <Row>
                    <Col>
                        <h1>Gallery</h1>
                    </Col>
                </Row>
                <Row>
                    <Col>
                        <Gallery/>
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default App;
