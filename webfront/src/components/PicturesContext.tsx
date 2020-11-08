import React from "react";
import axios, {AxiosResponse} from "axios";
import Pictures from "./Pictures";
import Loading from "./Loading";
import {picturesUrl, Picture as ApiPicture } from "../API";

export default function PicturesContext() {
    const [pictures, setPictures] = React.useState<ApiPicture[] | undefined>(undefined);
    const [loaded, setLoaded] = React.useState(false);

    React.useEffect(fetchPicturesList, []);

    function fetchPicturesList() {
        axios.get(picturesUrl())
            .then((response: AxiosResponse<ApiPicture[]>) => {
                const {data} = response;
                setPictures(data);
                setLoaded(true);
            })
            .catch(e => console.log(e));
    }

    return (
        <>
            {
                loaded && typeof pictures !== "undefined" ?
                <Pictures pictures={pictures}/> :
                <Loading/>
            }
        </>
    );
}