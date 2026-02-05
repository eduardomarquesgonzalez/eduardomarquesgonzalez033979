import { lazy, Suspense } from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import AuthGuard from '../guards/AuthGuard'
import MainLayout from '../layout/MainLayout'
import LoadingSpinner from '../../shared/components/LoadingSpinner'
import AlbumForm from '@/modules/albums/pages/AlbumForm'


// Lazy loading
const Login = lazy(() => import('../../modules/auth/pages/Login'))
const Register = lazy(() => import('../../modules/auth/pages/Register'))
const ArtistList = lazy(() => import('../../modules/artists/pages/ArtistList'))
const ArtistDetail = lazy(() => import('../../modules/artists/pages/ArtistDetail'))
const ArtistForm = lazy(() => import('../../modules/artists/pages/ArtistForm'))

const AlbumList  = lazy(() => import('../../modules/albums/pages/AlbumList'))
const AlbumDetail  = lazy(() => import('../../modules/albums/pages/AlbumDetail'))

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
            <Route path="/artists/new" element={<ArtistForm />} />
            <Route path="/artists/:id" element={<ArtistDetail />} />
            <Route path="/artists/:id/edit" element={<ArtistForm />} />

            <Route path="/albums" element={<AlbumList />} />
            <Route path="/albums/new" element={<AlbumForm />} />
            <Route path="/albums/:id" element={<AlbumDetail />} />
            <Route path="/albums/:id/edit" element={<AlbumForm />} />
          </Route>
        </Route>
      </Routes>
    </Suspense>
  )
}