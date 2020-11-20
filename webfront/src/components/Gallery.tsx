import React from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faTh, faThList, faEdit, faSave} from "@fortawesome/free-solid-svg-icons";
import PicturesProvider from "./PicturesProvider";
import GridPictures from "./GridPictures";
import ListPictures from "./ListPictures";
import {Picture as ApiPicture} from "../API";

export default function Gallery() {
    const [viewMode, setViewMode] = React.useState('grid');
    const [editMode, setEditMode] = React.useState(false);

    interface ViewMode {
        name: string,
        next: string,
        renderFn: (pictures: ApiPicture[]) => JSX.Element,
        markup: JSX.Element
    }

    const viewModes: ViewMode[] = [
        {
            name: "grid",
            next: "list",
            renderFn: ((pictures: ApiPicture[]) => <GridPictures pictures={pictures}/>),
            markup: <FontAwesomeIcon style={{cursor: "pointer"}} onClick={toggleViewMode} icon={faThList}/>
        }, {
            name: "list",
            next: "grid",
            renderFn: ((pictures: ApiPicture[]) => <ListPictures pictures={pictures} editMode={editMode}/>),
            markup:
                <>
                    {editMode ?
                        <FontAwesomeIcon style={{cursor: "pointer"}} onClick={toggleEditMode} icon={faSave}
                                         className="mr-2"/> :
                        <FontAwesomeIcon style={{cursor: "pointer"}} onClick={toggleEditMode} icon={faEdit}
                                         className="mr-2"/>
                    }
                    <FontAwesomeIcon style={{cursor: "pointer"}} onClick={toggleViewMode} icon={faTh}/>
                </>
        }
    ]
    function toggleViewMode() {
        // @ts-ignore
        setViewMode(viewModes.find((v) => v.name === viewMode).next);

    }
    function toggleEditMode() {
        setEditMode(!editMode);
    }

    function icon() {
        // @ts-ignore
        return viewModes.find((v) => v.name === viewMode).markup;
    }

    function renderer() {
        // @ts-ignore
        return viewModes.find((v) => v.name === viewMode).renderFn;
    }

    return (
        <>
            <div className="mt-4 mb-4 p-1" style={{ textAlign: 'right'}}>
                {icon()}
            </div>

            <PicturesProvider
                renderer={renderer()}
            />
        </>
    );
}
