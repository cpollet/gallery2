import React from "react";
import {Picture as ApiPicture} from "../API";

type propsType = {
    picture: ApiPicture
};

export default function Picture({picture}: propsType) {
    function thumbnail(picture: ApiPicture): string {
        const pic = picture.links.find(link => link.rel === "thumbnail-500x500-data");
        if (typeof pic != 'undefined') {
            return pic.href;
        }
        return "/noimage.png";
    }

    function main(picture: ApiPicture): string {
        const pic = picture.links.find(link => link.rel === "main-data");
        if (typeof pic != 'undefined') {
            return pic.href;
        }
        return "/noimage.png";
    }

    return (
        <div style={{float:"left"}}>
            <div>
            <a href={main(picture)}>
                <img
                    src={thumbnail(picture)}
                    alt={picture.name}/>
            </a>
            </div>
            <div>
                <strong>{picture.name}</strong>
                <p>{picture.description}</p>
            </div>
        </div>
    );
}