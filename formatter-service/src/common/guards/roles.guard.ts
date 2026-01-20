import { Injectable, CanActivate, ExecutionContext } from '@nestjs/common';
import { Reflector } from '@nestjs/core';
import { ROLES_KEY } from '../decorators/roles.decorator';

@Injectable()
export class RolesGuard implements CanActivate {
  constructor(private reflector: Reflector) {}

  canActivate(context: ExecutionContext): boolean {
    console.log('============================================');
    console.log('  ROLES GUARD ACTIVATED');
    console.log('============================================');
    
    const requiredRoles = this.reflector.getAllAndOverride<string[]>(ROLES_KEY, [
      context.getHandler(),
      context.getClass(),
    ]);
    
    console.log('Required Roles:', requiredRoles);
    
    if (!requiredRoles) {
      console.log(' No roles required, allowing access');
      console.log('============================================');
      return true;
    }
    
    const request = context.switchToHttp().getRequest();
    const user = request.user;
    
    console.log('User:', user);
    console.log('User Roles:', user?.realm_access?.roles);
    
    if (!user) {
      console.log(' No user found in request');
      console.log('============================================');
      return false;
    }
    
    const hasRole = requiredRoles.some(role =>
      user.realm_access?.roles?.includes(role)
    );
    
    console.log('User has required role?', hasRole);
    console.log('============================================');
    
    return hasRole;
  }
}