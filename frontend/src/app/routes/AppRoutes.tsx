import { Suspense, lazy } from "react";
import { Navigate, Route, Routes } from "react-router-dom";

import MainLayout from "../layout/MainLayout";
import LoadingSpinner from "@/shared/components/LoadingSpinner";

// Guards
import AuthGuard from "../guards/AuthGuard";
import RoleGuard from "../guards/RoleGuard";

const Login = lazy(() => import("@/modules/auth/pages/Login"));
const Register = lazy(() => import("@/modules/auth/pages/Register"));
const ArtistList = lazy(() => import("@/modules/artists/pages/ArtistList"));
const ArtistDetail = lazy(() => import("@/modules/artists/pages/ArtistDetail"));
const ArtistForm = lazy(() => import("@/modules/artists/pages/ArtistForm"));
const AlbumList = lazy(() => import("@/modules/albums/pages/AlbumList"));
const AlbumDetail = lazy(() => import("@/modules/albums/pages/AlbumDetail"));
const AlbumForm = lazy(() => import("@/modules/albums/pages/AlbumForm"));

export default function AppRoutes() {
  return (
    <Suspense fallback={<LoadingSpinner />}>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        <Route element={<AuthGuard />}>
          <Route element={<MainLayout />}>
            <Route path="/" element={<Navigate to="/artists" replace />} />

            <Route path="/artists" element={<ArtistList />} />
            <Route path="/artists/:id" element={<ArtistDetail />} />
            <Route path="/albums" element={<AlbumList />} />
            <Route path="/albums/:id" element={<AlbumDetail />} />

            <Route element={<RoleGuard allowedRoles={["ADMIN"]} />}>
              <Route path="/artists/new" element={<ArtistForm />} />
              <Route path="/artists/:id/edit" element={<ArtistForm />} />
              <Route path="/albums/new" element={<AlbumForm />} />
              <Route path="/albums/:id/edit" element={<AlbumForm />} />
            </Route>
          </Route>
        </Route>

        <Route path="*" element={<Navigate to="/artists" replace />} />
      </Routes>
    </Suspense>
  );
}
