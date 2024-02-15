import React from "react";
import {Picture as ApiPicture} from "../API";
import Col from "react-bootstrap/Col";
import AutocompleteTextarea from "./AutocompleteTextarea";
import Description from "./Description";

interface PropsType {
    inEditMode: boolean,
    picture: ApiPicture
    availableTag: {
        tag: string,
        count: number
    }[],
    onChange: (p: ApiPicture) => void
}

export default function EditablePicture({inEditMode, picture, availableTag, onChange}: PropsType) {
    function thumbnail(picture: ApiPicture): string {
        const pic = picture.links.find(link => link.rel === "thumbnail-450x450-white-data");
        if (typeof pic != 'undefined') {
            return pic.href;
        }
        return "/noimage.png";
    }

    const [originalDescription, setOriginalDescription] = React.useState(picture.description);

    function onChangeIfUpdated(description: string) {
        if (originalDescription !== description) {
            onChange({
                ...picture,
                description
            });
            setOriginalDescription(description);
        }
    }

    return (
        <>
            <Col className="col-3">
                <img src={thumbnail(picture)} width="100%"/>
            </Col>
            <Col>
                <h5>{picture.name}</h5>
                <div>
                    {inEditMode ?
                        <AutocompleteTextarea
                            text={picture.description}
                            tags={availableTag}
                            onChange={(text) => onChangeIfUpdated(text)}
                        /> :

                        <div style={{padding: '1px'}}>
                            <Description tagRegions={picture.tags}>
                                {picture.description}
                            </Description>
                        </div>
                    }
                </div>
            </Col>
        </>
    );
}