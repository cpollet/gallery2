import React from "react";
import {Picture as ApiPicture} from "../API";
import Picture from "./Picture";

type PropsType = {
    pictures: ApiPicture[]
}

export default function Pictures({pictures}: PropsType) {
    return (
        <>
            {pictures?.map(p => <Picture picture={p}/>)}
        </>
    );
}