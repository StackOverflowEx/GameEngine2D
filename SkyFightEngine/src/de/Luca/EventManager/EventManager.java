package de.Luca.EventManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.Luca.Events.Event;

public class EventManager {
	
	public static EventManager eventMangaer;
	
	private HashMap<Listener, HashMap<Class<? extends Event>, List<Method>>> listeners = new HashMap<Listener, HashMap<Class<? extends Event>, List<Method>>>();
	
	
	//Erstellt einen EventManager
	public EventManager() {
		if(eventMangaer != null) {
			throw new IllegalAccessError("EventManager already created");
		}
		eventMangaer = this;
	}
	
	//Es können Listener hinzugefügt werden, in welchen deklaiert werden kann, was bei bestimmten Events passiert.
	public void registerEvent(Listener listener) {
		listeners.put(listener, createRegisteredListener(listener));
	}
	
	public void fireEvent(Event e) {
		final Class<? extends Event> eventClass = e.getClass().asSubclass(Event.class);
		for(Listener l : listeners.keySet()) {
			if(listeners.get(l).containsKey(eventClass)) {
				for(Method m : listeners.get(l).get(eventClass)) {
					try {
						m.invoke(l, e);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
	
	//Adds all Methods with annotation "EventHandler" to List
	private HashMap<Class<? extends Event>, List<Method>> createRegisteredListener(Listener listener) {
		HashMap<Class<? extends Event>, List<Method>> methods = new HashMap<Class<? extends Event>, List<Method>>();
		Method[] publicMethod = listener.getClass().getMethods();
		for(Method m : publicMethod) {
			if(m.getAnnotation(EventHandler.class) != null) {
				if(!m.isBridge() && !m.isSynthetic()) {
					if(m.getParameterTypes().length == 1) {
						final Class<?> checkClass;
						if(Event.class.isAssignableFrom(checkClass = m.getParameterTypes()[0])) {
							 final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);
							 m.setAccessible(true);
							 
							 if(!methods.containsKey(eventClass)) {
								 List<Method> l = new ArrayList<Method>();
								 methods.put(eventClass, l);
							 }
							 List<Method> l = methods.get(eventClass);
							 l.add(m);
						}
					}
				}
			}
		}
		return methods;
	}
	
	//Es können Listener entfernt werden.
	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}

}
