import React from "react";
import axios, {AxiosResponse} from "axios";
import Loading from "./Loading";
import {picturesUrl, Picture as ApiPicture } from "../API";

interface PropsType {
    renderer: (pictures: ApiPicture[]) => JSX.Element
}

export function save(p: ApiPicture) {
    console.log("Saving ", p);
}

export default function PicturesProvider({renderer}: PropsType) {
    const [pictures, setPictures] = React.useState<ApiPicture[]>([]);
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
                loaded ? renderer(pictures) : <Loading/>
            }
        </>
    );
}