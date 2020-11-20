import React from "react";
import {Picture as ApiPicture} from "../API";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import Description from "./Description";
import Form from "react-bootstrap/Form";

interface PropsType {
    pictures: ApiPicture[],
    editMode: boolean
}

export default function ListPictures({pictures, editMode}: PropsType) {
    function thumbnail(picture: ApiPicture): string {
        const pic = picture.links.find(link => link.rel === "thumbnail-450x450-white-data");
        if (typeof pic != 'undefined') {
            return pic.href;
        }
        return "/noimage.png";
    }

    return (
        <Row>
            <Col>
                {pictures?.map(p => (
                    <Row key={p.id} className="mb-4">
                        <Col className="col-3">
                            <img src={thumbnail(p)} width="100%"/>
                        </Col>
                        <Col>
                            <h5>{p.name}</h5>
                            <p>
                                {editMode ?
                                    <Form.Control as="textarea" rows={3}>
                                        {p.description}
                                    </Form.Control> :
                                    <Description tagRegions={p.tags}>
                                        {p.description}
                                    </Description>
                                }
                            </p>
                        </Col>
                    </Row>
                ))}
            </Col>
        </Row>
    );
}