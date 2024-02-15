import React from "react";
import {AvailableTag as ApiAvailableTag, Picture as ApiPicture} from "../API";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import EditablePicture from "./EditablePicture";
import AvailableTagsProvider from "./AvailableTagsProvider";
import {save as savePicture } from "./PicturesProvider";

interface PropsType {
    pictures: ApiPicture[],
    inEditMode: boolean
}

export default function ListPictures({pictures, inEditMode}: PropsType) {
    const renderer = (tags: ApiAvailableTag[]) => (
        <Row>
            <Col>
                {pictures?.map(p => (
                    <Row key={p.id} className="mb-4">
                        <EditablePicture
                            inEditMode={inEditMode}
                            picture={p}
                            availableTag={tags}
                            onChange={savePicture}
                        />
                    </Row>
                ))}
            </Col>
        </Row>
    );

    return (
        <AvailableTagsProvider renderer={renderer}/>
    );
}