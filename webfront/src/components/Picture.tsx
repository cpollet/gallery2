import React from "react";
import {Picture as ApiPicture} from "../API";
import Card from "react-bootstrap/Card";
import './Picture.css';
import Description from "./Description";

interface propsType {
    picture: ApiPicture
}

export default function Picture({picture}: propsType) {
    function thumbnail(picture: ApiPicture): string {
        const pic = picture.links.find(link => link.rel === "thumbnail-450x450-white-data");
        if (typeof pic != 'undefined') {
            return pic.href;
        }
        return "/noimage.png";
    }

    return (
        <Card className="mb-4">
            <Card.Img variant="top" src={thumbnail(picture)}/>
            <Card.Body>
                <Card.Title>{picture.name}</Card.Title>
                <Card.Text className="light-text">
                    <Description tagRegions={picture.tags}>
                        {picture.description}
                    </Description>
                </Card.Text>
                <Card.Link href="#">edit</Card.Link>
            </Card.Body>
        </Card>
    );
}